package net.simplifiedcoding.ui.home

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.ResultReceiver
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import net.simplifiedcoding.R
import net.simplifiedcoding.data.models.VideoContent
import net.simplifiedcoding.ui.MainActivity
import net.simplifiedcoding.ui.utils.getRootDirPath


class DownloadService : IntentService("DownloadService") {


    private lateinit var receiver: ResultReceiver

    override fun onHandleIntent(intent: Intent?) {
        val videoContent = intent!!.getSerializableExtra(VideosFragment.KEY_VIDEO) as VideoContent
        receiver =
            intent.getParcelableExtra<Parcelable>(VideosFragment.KEY_RECEIVER) as ResultReceiver

        if (videoContent.sources.isNotEmpty()) {
            videoContent.isDownloading = true
            val fileName = getFileName(videoContent.sources[0])
            videoContent.downloadID =
                PRDownloader.download(
                    videoContent.sources[0],
                    getRootDirPath(applicationContext),
                    fileName
                )
                    .build()
                    .setOnStartOrResumeListener {
                        showDownloadNotification(videoContent)
                    }
                    .setOnPauseListener { }
                    .setOnCancelListener { }
                    .setOnProgressListener {
                        videoContent.progress = (it.currentBytes * 100 / it.totalBytes).toInt()
                        sendDataToUI(videoContent)
                        if (videoContent.progress % 25 == 0)
                            showDownloadNotification(videoContent)
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            videoContent.isDownloading = false
                            videoContent.isDownloaded = true
                            sendDataToUI(videoContent)
                            showDownloadNotification(videoContent)
                        }

                        override fun onError(error: com.downloader.Error?) {}
                    })
            sendDataToUI(videoContent)
        }
    }

    private fun showDownloadNotification(videoContent: VideoContent) {
        val builder = NotificationCompat.Builder(this, MainActivity.CHANNEL_ID).apply {
            setContentTitle("File Download")
            setContentText("Download in progress")
            setSmallIcon(R.drawable.ic_download)
            setOnlyAlertOnce(true)
            priority = NotificationCompat.PRIORITY_LOW
        }

        NotificationManagerCompat.from(this).apply {
            if (videoContent.isDownloaded) {
                builder.setContentText("Download Complete")
                    .setProgress(0, 0, false)
                notify(videoContent.position, builder.build())
            } else {
                builder.setProgress(PROGRESS_MAX, videoContent.progress, false)
                notify(videoContent.position, builder.build())
            }
        }
    }

    private fun sendDataToUI(videoContent: VideoContent) {
        val resultData = Bundle()
        resultData.putSerializable(VideosFragment.KEY_VIDEO, videoContent)
        receiver.send(UPDATE_PROGRESS, resultData)
    }

    private fun getFileName(url: String): String =
        url.substring(url.lastIndexOf("/") + 1, url.length)

    companion object {
        const val UPDATE_PROGRESS = 8344
        const val PROGRESS_MAX = 100
    }
}
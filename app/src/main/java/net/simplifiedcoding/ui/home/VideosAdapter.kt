package net.simplifiedcoding.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import net.simplifiedcoding.R
import net.simplifiedcoding.data.models.VideoContent
import net.simplifiedcoding.databinding.RecyclerViewVideoBinding
import net.simplifiedcoding.ui.utils.getRootDirPath

class VideosAdapter : RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {

    private var videos = listOf<VideoContent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VideoViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_view_video,
            parent,
            false
        )
    )

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.binding.video = videos[position]
        holder.binding.imageButtonDownload.setOnClickListener {
            if (videos[position].isDownloading) {
                Log.d("DownloadingCancel", "Cancelled")
                videos[position].isDownloading = false
                PRDownloader.cancel(videos[position].downloadID)
            } else {
                downloadVideo(videos[position], getRootDirPath(holder.binding.root.context))
            }
            notifyDataSetChanged()
        }

        holder.binding.executePendingBindings()
    }

    fun setVideos(videos: List<VideoContent>) {
        this.videos = videos
        notifyDataSetChanged()
    }

    private fun getFileName(url: String): String =
        url.substring(url.lastIndexOf("/") + 1, url.length)


    private fun downloadVideo(videoContent: VideoContent, dirPath: String) {
        if (videoContent.sources.isNotEmpty()) {
            videoContent.isDownloading = true
            val fileName = getFileName(videoContent.sources[0])
            videoContent.downloadID =
                PRDownloader.download(videoContent.sources[0], dirPath, fileName)
                    .build()
                    .setOnStartOrResumeListener { }
                    .setOnPauseListener { }
                    .setOnCancelListener { }
                    .setOnProgressListener {
                        videoContent.progress = (it.currentBytes * 100 / it.totalBytes).toInt()
                        notifyDataSetChanged()
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {

                        }

                        override fun onError(error: com.downloader.Error?) {}
                    })

        }
    }

    inner class VideoViewHolder(val binding: RecyclerViewVideoBinding) :
        RecyclerView.ViewHolder(binding.root)
}
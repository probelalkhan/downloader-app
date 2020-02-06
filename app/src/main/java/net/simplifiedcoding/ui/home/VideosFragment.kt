package net.simplifiedcoding.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.downloader.PRDownloader
import kotlinx.android.synthetic.main.fragment_videos.*
import kotlinx.coroutines.launch
import net.simplifiedcoding.R
import net.simplifiedcoding.data.models.VideoContent
import net.simplifiedcoding.ui.base.BaseFragment
import org.kodein.di.generic.instance


class VideosFragment : BaseFragment(), RecyclerViewButtonClickListener {

    private val factory: VideosViewModelFactory by instance()
    private lateinit var videosAdapter: VideosAdapter
    private lateinit var viewModel: VideosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_videos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(VideosViewModel::class.java)
        videosAdapter = VideosAdapter()
        videosAdapter.listener = this
        recycler_view_videos.adapter = videosAdapter

        fetchAllVideos()

        viewModel.videos.observe(viewLifecycleOwner, Observer {
            videosAdapter.setVideos(it)
        })
    }

    private fun fetchAllVideos() = launch {
        viewModel.fetchAllVideos()
    }

    override fun onRecyclerViewItemClick(view: View, video: VideoContent) {
        if (!video.isDownloading) {
            downloadVideo(video)
        } else {
            PRDownloader.cancel(video.downloadID)
            video.isDownloading = false
            videosAdapter.notifyItemChanged(video.position)
        }
    }

    private fun downloadVideo(video: VideoContent) {
        Intent(requireContext(), DownloadService::class.java).also {
            it.putExtra(KEY_VIDEO, video)
            it.putExtra(KEY_RECEIVER, DownloadReceiver(Handler()))
            requireContext().startService(it)
        }
    }

    inner class DownloadReceiver(handler: Handler?) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            super.onReceiveResult(resultCode, resultData)
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                val video = resultData.getSerializable(KEY_VIDEO) as VideoContent
                videosAdapter.setVideo(video)
            }
        }
    }

    companion object {
        const val KEY_VIDEO = "key_video"
        const val KEY_RECEIVER = "key_receiver"
    }
}

package net.simplifiedcoding.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import kotlinx.android.synthetic.main.fragment_videos.*
import kotlinx.coroutines.launch
import net.simplifiedcoding.R
import net.simplifiedcoding.data.models.VideoContent
import net.simplifiedcoding.databinding.RecyclerViewVideoBinding
import net.simplifiedcoding.ui.base.BaseFragment
import net.simplifiedcoding.ui.utils.getRootDirPath
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

    override fun onRecyclerViewItemClick(binding: RecyclerViewVideoBinding, video: VideoContent) {
        if (!video.isDownloading) {
            downloadVideo(binding, video, getRootDirPath(requireContext()))
        } else {
            PRDownloader.cancel(video.downloadID)
            video.isDownloading = false
            binding.video = video
            binding.executePendingBindings()
        }
    }

    private fun getFileName(url: String): String =
        url.substring(url.lastIndexOf("/") + 1, url.length)

    private fun downloadVideo(
        binding: RecyclerViewVideoBinding,
        videoContent: VideoContent,
        dirPath: String
    ) {
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
                        updateRecyclerViewItemUI(binding, videoContent)
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            videoContent.isDownloaded = true
                            updateRecyclerViewItemUI(binding, videoContent)
                        }

                        override fun onError(error: com.downloader.Error?) {}
                    })
            updateRecyclerViewItemUI(binding, videoContent)
        }
    }

    private fun updateRecyclerViewItemUI(
        binding: RecyclerViewVideoBinding,
        videoContent: VideoContent
    ) {
        binding.video = videoContent
        binding.executePendingBindings()
    }
}

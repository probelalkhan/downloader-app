package net.simplifiedcoding.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import net.simplifiedcoding.data.models.VideoContent
import net.simplifiedcoding.data.repository.VideoContentRepository


class VideosViewModel(
    private val repository: VideoContentRepository
) : ViewModel() {

    private val _videos = MutableLiveData<List<VideoContent>>()
    val videos: LiveData<List<VideoContent>>
        get() = _videos

    suspend fun fetchAllVideos() {
        _videos.value = repository.getAllVideos()
    }

}

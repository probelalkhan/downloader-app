package net.simplifiedcoding.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.simplifiedcoding.data.repository.VideoContentRepository

@Suppress("UNCHECKED_CAST")
class VideosViewModelFactory(
    private val repository: VideoContentRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VideosViewModel(repository) as T
    }

}
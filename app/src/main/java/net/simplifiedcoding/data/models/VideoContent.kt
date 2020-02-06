package net.simplifiedcoding.data.models

data class VideoContent(
    val description: String,
    val sources: List<String>,
    val subtitle: String,
    val thumb: String,
    val title: String,
    var downloadID: Int,
    var progress: Int = 0,
    var isDownloading: Boolean = false,
    var isDownloaded: Boolean = false
)
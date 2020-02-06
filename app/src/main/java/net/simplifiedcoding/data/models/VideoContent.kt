package net.simplifiedcoding.data.models

import java.io.Serializable

data class VideoContent(
    val description: String,
    val sources: List<String>,
    val subtitle: String,
    val thumb: String,
    val title: String,
    var downloadID: Int,
    var progress: Int = 0,
    var isDownloading: Boolean = false,
    var isDownloaded: Boolean = false,
    var position: Int = -1
) : Serializable
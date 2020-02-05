package net.simplifiedcoding.data.repository

import net.simplifiedcoding.data.network.DownloaderAppApi
import net.simplifiedcoding.data.network.SafeApiRequest

class VideoContentRepository(
    private val api: DownloaderAppApi
) : SafeApiRequest() {

    suspend fun getAllVideos() = apiRequest { api.getAllVideos() }

}
package net.simplifiedcoding.data.network

import net.simplifiedcoding.BuildConfig
import net.simplifiedcoding.data.models.VideoContent
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface DownloaderAppApi {

    @GET("allVideos")
    suspend fun getAllVideos(): Response<List<VideoContent>>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): DownloaderAppApi {

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(BuildConfig.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DownloaderAppApi::class.java)
        }
    }
}
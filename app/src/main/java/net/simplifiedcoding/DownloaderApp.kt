package net.simplifiedcoding

import android.app.Application
import com.downloader.PRDownloader
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import net.simplifiedcoding.data.network.DownloaderAppApi
import net.simplifiedcoding.data.network.NetworkConnectionInterceptor
import net.simplifiedcoding.data.repository.VideoContentRepository
import net.simplifiedcoding.ui.home.VideosViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class DownloaderApp : Application(), KodeinAware {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        PRDownloader.initialize(this)
        configureFresco()
    }

    private fun configureFresco() {
        val config = ImagePipelineConfig.newBuilder(this)
            .setDownsampleEnabled(true)
            .build()
        Fresco.initialize(this, config)
    }


    override val kodein = Kodein.lazy {
        import(androidXModule(this@DownloaderApp))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { DownloaderAppApi(instance()) }

        bind() from provider { VideoContentRepository(instance()) }
        bind() from provider { VideosViewModelFactory(instance()) }
    }
}
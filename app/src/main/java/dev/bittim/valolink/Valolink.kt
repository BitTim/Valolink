package dev.bittim.valolink

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.transition.CrossfadeTransition
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import dev.bittim.valolink.core.ui.AnimatedPngDecoder
import javax.inject.Inject

@HiltAndroidApp
class Valolink : Application(), ImageLoaderFactory, Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun newImageLoader(): ImageLoader {
        return ImageLoader
            .Builder(this)
            .memoryCache {
                MemoryCache.Builder(this).maxSizePercent(0.20).build()
            }
            .diskCache {
                DiskCache
                    .Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(100 * 1024 * 1024) // 100 MB
                    .build()
            }
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .transitionFactory(CrossfadeTransition.Factory())
            .crossfade(true)
            .components {
                add(AnimatedPngDecoder.Factory())
                add(ImageDecoderDecoder.Factory())
            }
            .build()
    }
}
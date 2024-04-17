package dev.bittim.valolink

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.annotation.ExperimentalCoilApi
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Valolink : Application(), ImageLoaderFactory {
    @OptIn(ExperimentalCoilApi::class)
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this).memoryCache {
                MemoryCache.Builder(this).maxSizePercent(0.20).build()
            }.diskCache {
                DiskCache.Builder().directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(5 * 1024 * 1024).build()
            }.logger(DebugLogger()).respectCacheHeaders(false).build()
    }
}
/*
Copyright (c) 2024 BitTim

Project:        Valolink
License:        GPLv3

File:           AnimatedPngDecoder.kt
Author:         Tim Anhalt (BitTim) - Modified from source
Source:         colinrtwhite (https://github.com/coil-kt/coil/issues/506#issuecomment-952526682)
Created:        11.05.2024
Description:    Decoder to load animated PNGs with coil.
*/

package dev.bittim.valolink.core.ui.decoder

import coil.ImageLoader
import coil.decode.DecodeResult
import coil.decode.Decoder
import coil.decode.ImageSource
import coil.fetch.SourceResult
import coil.request.Options
import com.linecorp.apng.ApngDrawable

class AnimatedPngDecoder(private val source: ImageSource) : Decoder {
    override suspend fun decode(): DecodeResult {
        return DecodeResult(
            drawable = ApngDrawable.decode(source.file().toFile()),
            isSampled = false
        )
    }

    class Factory : Decoder.Factory {
        override fun create(
            result: SourceResult,
            options: Options,
            imageLoader: ImageLoader,
        ): Decoder? {
            return if (ApngDrawable.isApng(result.source.file().toFile())) {
                AnimatedPngDecoder(result.source)
            } else {
                null
            }
        }
    }
}
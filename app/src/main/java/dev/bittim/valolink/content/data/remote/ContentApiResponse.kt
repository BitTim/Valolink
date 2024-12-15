/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ContentApiResponse.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContentApiResponse<T>(
    val status: Int,
    val data: T?,
    val message: String?,
)
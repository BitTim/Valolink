package dev.bittim.valolink.content.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContentApiResponse<T>(
    val status: Int,
    val data: T?,
    val message: String?,
)
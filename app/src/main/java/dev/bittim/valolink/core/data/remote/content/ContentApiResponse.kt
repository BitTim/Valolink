package dev.bittim.valolink.core.data.remote.content

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContentApiResponse<T>(
    val status: Int,
    val data: T?,
    val message: String?,
)
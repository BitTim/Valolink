package dev.bittim.valolink.feature.content.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GameApiResponse<T>(
    val status: Int,
    val data: T?,
    val message: String?
)
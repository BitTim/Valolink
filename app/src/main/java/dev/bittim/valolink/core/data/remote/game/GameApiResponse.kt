package dev.bittim.valolink.core.data.remote.game

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GameApiResponse<T>(
    val status: Int,
    val data: T?,
    val message: String?,
)
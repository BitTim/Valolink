package dev.bittim.valolink.feature.content.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeasonDto(
    val uuid: String,
    val displayName: String,
    val type: String?,
    val startTime: String,
    val endTime: String,
    val parentUuid: String?,
    val assetPath: String
)
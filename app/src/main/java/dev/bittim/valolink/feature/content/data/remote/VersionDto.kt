package dev.bittim.valolink.feature.content.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VersionDto(
    val manifestId: String,
    val branch: String,
    val version: String,
    val buildVersion: String,
    val engineVersion: String,
    val riotClientVersion: String,
    val riotClientBuild: String,
    val buildDate: String
)

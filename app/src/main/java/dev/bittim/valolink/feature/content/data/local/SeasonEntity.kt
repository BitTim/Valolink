package dev.bittim.valolink.feature.content.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SeasonEntity(
    @PrimaryKey
    val uuid: String,
    val version: String,
    val displayName: String,
    val type: String?,
    val startTime: String,
    val endTime: String,
    val parentUuid: String?
)

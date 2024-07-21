package dev.bittim.valolink.main.domain.model.user

data class ProgressionLevel(
    val uuid: String,
    val progression: String,
    val level: String,
    val isPurchased: Boolean,
)

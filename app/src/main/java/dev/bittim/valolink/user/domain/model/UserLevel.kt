package dev.bittim.valolink.user.domain.model

data class UserLevel(
    val uuid: String,
    val userContract: String,
    val level: String,
    val isPurchased: Boolean,
)

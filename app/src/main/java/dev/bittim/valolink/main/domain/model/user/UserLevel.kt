package dev.bittim.valolink.main.domain.model.user

data class UserLevel(
    val uuid: String,
    val userContract: String,
    val level: String,
    val isPurchased: Boolean,
)

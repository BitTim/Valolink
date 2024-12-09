package dev.bittim.valolink.user.domain.model

data class UserContract(
    val uuid: String,
    val user: String,
    val contract: String,
    val levels: List<UserLevel>,
)

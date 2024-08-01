package dev.bittim.valolink.main.domain.model.user

data class UserContract(
    val uuid: String,
    val user: String,
    val contract: String,
    val levels: List<UserLevel>,
)

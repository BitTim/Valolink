package dev.bittim.valolink.main.domain.model.user

data class UserData(
    val uuid: String,
    val isPrivate: Boolean,
    val username: String,
    val agents: List<UserAgent>,
    val contracts: List<UserContract>,
)

package dev.bittim.valolink.main.domain.model

data class UserData(
    val uuid: String,
    val username: String,
    val agents: List<String>,
)

package dev.bittim.valolink.feature.main.domain.model

data class UserData(
    val uuid: String,
    val username: String,
    val agents: List<String>,
)

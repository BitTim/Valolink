package dev.bittim.valolink.main.domain.model

data class UserData(
    val uuid: String,
    val isPrivate: Boolean,
    val username: String,
    val agents: List<String>,
    val gears: List<Gear>,
)

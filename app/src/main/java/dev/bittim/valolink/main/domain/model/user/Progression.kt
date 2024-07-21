package dev.bittim.valolink.main.domain.model.user

data class Progression(
    val uuid: String,
    val user: String,
    val contract: String,
    val levels: List<ProgressionLevel>,
)

package dev.bittim.valolink.main.domain.model.game.agent

data class Ability(
    val agent: String,
    val slot: String,
    val displayName: String,
    val description: String,
    val displayIcon: String?,
)
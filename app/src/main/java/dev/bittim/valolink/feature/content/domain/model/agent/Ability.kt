package dev.bittim.valolink.feature.content.domain.model.agent

data class Ability(
    val agent: String,
    val slot: String,
    val displayName: String,
    val description: String,
    val displayIcon: String
)
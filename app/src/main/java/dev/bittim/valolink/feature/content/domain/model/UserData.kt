package dev.bittim.valolink.feature.content.domain.model

data class UserData(
    val hasOnboarded: Boolean = false,
    val ownedAgents: List<String> = listOf(),
)

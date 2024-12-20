package dev.bittim.valolink.main.data.remote.game.dto.agents

import dev.bittim.valolink.main.data.local.game.entity.agent.AbilityEntity

data class AbilityDto(
    val slot: String,
    val displayName: String,
    val description: String,
    val displayIcon: String?,
) {
    fun toEntity(
        version: String,
        agentUuid: String,
    ): AbilityEntity {
        return AbilityEntity(
            agentUuid + slot,
            agentUuid,
            version,
            slot,
            displayName,
            description,
            displayIcon
        )
    }
}
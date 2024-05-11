package dev.bittim.valolink.feature.content.data.remote.game.dto.buddy

import dev.bittim.valolink.feature.content.data.local.game.entity.buddy.BuddyLevelEntity

data class BuddyLevelDto(
    val uuid: String,
    val charmLevel: Int,
    val hideIfNotOwned: Boolean,
    val displayName: String,
    val displayIcon: String,
    val assetPath: String
) {
    fun toEntity(version: String): BuddyLevelEntity {
        return BuddyLevelEntity(
            uuid = uuid,
            version = version,
            hideIfNotOwned = hideIfNotOwned,
            displayName = displayName,
            displayIcon = displayIcon
        )
    }
}
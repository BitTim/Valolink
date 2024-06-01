package dev.bittim.valolink.main.data.remote.game.dto

import dev.bittim.valolink.main.data.local.game.entity.PlayerTitleEntity

data class PlayerTitleDto(
    val uuid: String,
    val displayName: String,
    val titleText: String,
    val isHiddenIfNotOwned: Boolean,
    val assetPath: String,
) {
    fun toEntity(version: String): PlayerTitleEntity {
        return PlayerTitleEntity(
            uuid = uuid,
            version = version,
            displayName = displayName,
            titleText = titleText,
            isHiddenIfNotOwned = isHiddenIfNotOwned
        )
    }
}
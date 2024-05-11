package dev.bittim.valolink.feature.content.data.remote.game.dto.weapon

import dev.bittim.valolink.feature.content.data.local.game.entity.weapon.WeaponSkinLevelEntity

data class WeaponSkinLevelDto(
    val uuid: String,
    val displayName: String,
    val levelItem: String?,
    val displayIcon: String,
    val streamedVideo: String?,
    val assetPath: String
) {
    fun toEntity(version: String): WeaponSkinLevelEntity {
        return WeaponSkinLevelEntity(
            uuid = uuid,
            version = version,
            displayName = displayName,
            levelItem = levelItem,
            displayIcon = displayIcon,
            streamedVideo = streamedVideo
        )
    }
}
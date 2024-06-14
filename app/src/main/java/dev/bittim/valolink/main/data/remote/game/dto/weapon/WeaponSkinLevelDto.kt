package dev.bittim.valolink.main.data.remote.game.dto.weapon

import dev.bittim.valolink.main.data.local.game.entity.weapon.skins.WeaponSkinLevelEntity

data class WeaponSkinLevelDto(
    val uuid: String,
    val displayName: String,
    val levelItem: String?,
    val displayIcon: String,
    val streamedVideo: String?,
    val assetPath: String,
) {
    fun toEntity(version: String): WeaponSkinLevelEntity {
        return WeaponSkinLevelEntity(
            uuid = uuid,
            version = version,
            weaponSkin = "", // TODO: Add actual value
            displayName = displayName,
            levelItem = levelItem,
            displayIcon = displayIcon,
            streamedVideo = streamedVideo
        )
    }
}
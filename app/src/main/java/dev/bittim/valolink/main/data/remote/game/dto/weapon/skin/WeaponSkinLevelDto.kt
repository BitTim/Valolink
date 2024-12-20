package dev.bittim.valolink.main.data.remote.game.dto.weapon.skin

import dev.bittim.valolink.main.data.local.game.entity.weapon.skins.WeaponSkinLevelEntity

data class WeaponSkinLevelDto(
    val uuid: String,
    val displayName: String,
    val levelItem: String?,
    val displayIcon: String?,
    val streamedVideo: String?,
    val assetPath: String,
) {
    fun toEntity(
        version: String,
        skinUuid: String,
        levelIndex: Int,
    ): WeaponSkinLevelEntity {
        return WeaponSkinLevelEntity(
            uuid = uuid,
            version = version,
            weaponSkin = skinUuid,
            levelIndex = levelIndex,
            displayName = displayName,
            levelItem = levelItem,
            displayIcon = displayIcon,
            streamedVideo = streamedVideo,
        )
    }
}
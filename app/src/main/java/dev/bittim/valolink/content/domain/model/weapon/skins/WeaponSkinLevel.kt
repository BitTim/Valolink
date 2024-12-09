package dev.bittim.valolink.content.domain.model.weapon.skins

data class WeaponSkinLevel(
    val uuid: String,
    val levelIndex: Int,
    val displayName: String,
    val levelItem: String?,
    val displayIcon: String?,
    val streamedVideo: String?,
)

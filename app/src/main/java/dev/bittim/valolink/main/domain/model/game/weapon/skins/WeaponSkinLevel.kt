package dev.bittim.valolink.main.domain.model.game.weapon.skins

data class WeaponSkinLevel(
    val uuid: String,
    val levelIndex: Int,
    val displayName: String,
    val levelItem: String?,
    val displayIcon: String?,
    val streamedVideo: String?,
)

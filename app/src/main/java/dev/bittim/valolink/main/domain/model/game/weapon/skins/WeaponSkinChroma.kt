package dev.bittim.valolink.main.domain.model.game.weapon.skins

data class WeaponSkinChroma(
    val uuid: String,
    val displayName: String,
    val displayIcon: String?,
    val fullRender: String,
    val swatch: String?,
    val streamedVideo: String?,
)

package dev.bittim.valolink.content.domain.model.weapon.skins

data class WeaponSkinChroma(
    val uuid: String,
    val chromaIndex: Int,
    val displayName: String,
    val displayIcon: String?,
    val fullRender: String,
    val swatch: String?,
    val streamedVideo: String?,
)

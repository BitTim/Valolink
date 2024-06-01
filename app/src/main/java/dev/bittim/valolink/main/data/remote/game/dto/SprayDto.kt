package dev.bittim.valolink.main.data.remote.game.dto

import dev.bittim.valolink.main.data.local.game.entity.SprayEntity

data class SprayDto(
    val uuid: String,
    val displayName: String,
    val category: String?,
    val themeUuid: String?,
    val isNullSpray: Boolean,
    val hideIfNotOwned: Boolean,
    val displayIcon: String,
    val fullIcon: String?,
    val fullTransparentIcon: String?,
    val animationPng: String?,
    val animationGif: String?,
    val assetPath: String,
    val levels: List<SprayLevelDto>,
) {
    data class SprayLevelDto(
        val uuid: String,
        val sprayLevel: Int,
        val displayName: String,
        val displayIcon: String,
        val assetPath: String,
    )



    fun toEntity(version: String): SprayEntity {
        return SprayEntity(
            uuid = uuid,
            version = version,
            displayName = displayName,
            themeUuid = themeUuid,
            hideIfNotOwned = hideIfNotOwned,
            displayIcon = displayIcon,
            fullIcon = fullIcon,
            fullTransparentIcon = fullTransparentIcon,
            animationPng = animationPng,
            animationGif = animationGif
        )
    }
}
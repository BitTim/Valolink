package dev.bittim.valolink.main.domain.model.game.weapon.skins

import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardRelation
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardType

data class WeaponSkin(
    val uuid: String,
    val displayName: String,
    val themeUuid: String,
    val contentTierUuid: String?,
    val displayIcon: String?,
    val wallpaper: String?,
    val chromas: List<WeaponSkinChroma>,
    val levels: List<WeaponSkinLevel>,
) {
    fun asRewardRelation(
        amount: Int,
        levelUuid: String,
    ): RewardRelation {
        val displayIcon = levels.find { it.uuid == levelUuid }?.displayIcon ?: displayIcon ?: ""

        return RewardRelation(
            uuid = uuid,
            type = RewardType.WEAPON_SKIN,
            amount = amount,
            displayName = displayName,
            displayIcon = displayIcon,
            previewImages = if (chromas.count() > 1) chromas.map { it.fullRender to it.swatch } else listOf(
                displayIcon to null
            ),
            background = wallpaper,
        )
    }
}
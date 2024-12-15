/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponSkin.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.domain.model.weapon.skins

import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType

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
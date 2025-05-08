/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       PlayerCard.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop169
import androidx.compose.material.icons.filled.CropPortrait
import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType

data class PlayerCard(
    val uuid: String,
    val displayName: String,
    val isHiddenIfNotOwned: Boolean,
    val themeUuid: String?,
    val displayIcon: String,
    val smallArt: String,
    val wideArt: String,
    val largeArt: String?,
) {
    fun asRewardRelation(amount: Int): RewardRelation {
        return RewardRelation(
            uuid = uuid,
            type = RewardType.PLAYER_CARD,
            amount = amount,
            displayName = displayName,
            previewImages = listOf(
                largeArt to Icons.Filled.CropPortrait,
                wideArt to Icons.Filled.Crop169
            ),
            displayIcon = displayIcon,
            background = displayIcon
        )
    }
}
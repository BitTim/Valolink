/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       PlayerCard.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 17:49
 */

package dev.bittim.valolink.content.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop169
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.outlined.Crop169
import androidx.compose.material.icons.outlined.CropPortrait
import dev.bittim.valolink.R
import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import dev.bittim.valolink.core.ui.util.ToggleIcon
import dev.bittim.valolink.core.ui.util.UiText

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
                largeArt to ToggleIcon(
                    Icons.Filled.CropPortrait,
                    Icons.Outlined.CropPortrait,
                    UiText.StringResource(R.string.levelDetails_variant_portrait)
                ),
                wideArt to ToggleIcon(
                    Icons.Filled.Crop169,
                    Icons.Outlined.Crop169,
                    UiText.StringResource(R.string.levelDetails_variant_landscape)
                )
            ),
            displayIcon = displayIcon,
            background = displayIcon
        )
    }
}
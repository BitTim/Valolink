/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       HeaderSection.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.MoneyOff
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bittim.valolink.R
import dev.bittim.valolink.content.domain.model.contract.reward.Reward
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.util.ToggleIcon
import dev.bittim.valolink.core.ui.util.UiText

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    levelHeaderData: LevelHeaderData?,
    rewards: List<Reward>?,
    onRewardSelected: (Int) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        LevelHeader(data = levelHeaderData)

        Spacer(modifier = Modifier.height(Spacing.l))

        Crossfade(
            rewards,
            label = "RewardSelector appearance"
        ) { checkedRewards ->
            if (checkedRewards != null && checkedRewards.count() > 1) {
                RewardSelector(
                    modifier = Modifier.fillMaxWidth(),
                    rewards = checkedRewards.map {
                        if (it.isFreeReward) ToggleIcon(
                            Icons.Filled.MoneyOff,
                            Icons.Outlined.MoneyOff,
                            UiText.StringResource(R.string.reward_free)
                        ) else ToggleIcon(
                            Icons.Filled.Star,
                            Icons.Outlined.Star,
                            UiText.StringResource(R.string.reward_premium)
                        )
                    },
                    onRewardSelected = onRewardSelected
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.xl))
    }
}

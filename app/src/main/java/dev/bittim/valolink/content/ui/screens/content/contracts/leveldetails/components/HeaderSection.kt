/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       HeaderSection.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:30
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.content.domain.model.contract.reward.Reward

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

        Spacer(modifier = Modifier.height(16.dp))

        Crossfade(
            rewards,
            label = "RewardSelector appearance"
        ) { checkedRewards ->
            if (checkedRewards != null && checkedRewards.count() > 1) {
                RewardSelector(
                    modifier = Modifier.fillMaxWidth(),
                    rewards = checkedRewards.map { if (it.isFreeReward) "Free Reward" to Icons.Default.MoneyOff else "Default" to Icons.Default.Star },
                    onRewardSelected = onRewardSelected
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RewardSelector.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:30
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.ui.theme.ValolinkTheme

@Composable
fun RewardSelector(
    modifier: Modifier = Modifier,
    rewards: List<Pair<String, ImageVector>>,
    onRewardSelected: (Int) -> Unit,
) {
    var selected by remember {
        mutableIntStateOf(0)
    }

    Row(
        modifier = modifier.border(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant,
            MaterialTheme.shapes.large
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, bottom = 8.dp, end = 8.dp)
        ) {
            Text(
                text = "Rewards in Level",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = rewards[selected].first,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            rewards.forEachIndexed { index, reward ->
                FilledTonalIconToggleButton(
                    checked = selected == index,
                    onCheckedChange = {
                        selected = index
                        onRewardSelected(index)
                    },
                ) {
                    Icon(reward.second, null)
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RewardSelectorPreview() {
    ValolinkTheme {
        Surface {
            RewardSelector(
                modifier = Modifier.fillMaxWidth(),
                rewards = listOf(
                    "Default" to Icons.Default.Star,
                    "Free Reward" to Icons.Default.MoneyOff
                ),
                onRewardSelected = {}
            )
        }
    }
}

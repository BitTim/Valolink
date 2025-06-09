/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RewardSelector.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
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
import androidx.compose.material.icons.outlined.MoneyOff
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.components.ConnectedButtonEntry
import dev.bittim.valolink.core.ui.components.SingleConnectedButtonGroup
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.ToggleIcon
import dev.bittim.valolink.core.ui.util.UiText

@Composable
fun RewardSelector(
    modifier: Modifier = Modifier,
    rewards: List<ToggleIcon>,
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
            modifier = Modifier
                .weight(1f)
                .padding(top = Spacing.s, start = Spacing.l, bottom = Spacing.s, end = Spacing.s)
        ) {
            Text(
                text = UiText.StringResource(R.string.levelDetails_rewardSelector_title).asString(),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = rewards[selected].contentDescription?.asString() ?: "?",
                style = MaterialTheme.typography.labelMedium
            )
        }

        SingleConnectedButtonGroup(
            modifier = Modifier
                .weight(1f)
                .padding(Spacing.s),
            entries = rewards.map {
                ConnectedButtonEntry(
                    label = null,
                    icon = it
                )
            },
            onSelectedChanged = {
                selected = it
                onRewardSelected(it)
            }
        )
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
                    ToggleIcon(
                        Icons.Filled.Star,
                        Icons.Outlined.Star,
                        UiText.StringResource(R.string.reward_premium)
                    ),
                    ToggleIcon(
                        Icons.Filled.MoneyOff,
                        Icons.Outlined.MoneyOff,
                        UiText.StringResource(R.string.reward_free)
                    )
                ),
                onRewardSelected = {}
            )
        }
    }
}

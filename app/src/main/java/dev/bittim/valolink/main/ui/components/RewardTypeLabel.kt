/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       RewardTypeLabel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.main.ui.components

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.main.ui.util.getScaledLineHeightFromFontStyle

enum class RewardTypeLabelStyle {
    DEFAULT,
    SMALL
}

@Composable
fun RewardTypeLabel(
    modifier: Modifier = Modifier,
    rewardType: RewardType?,
    style: RewardTypeLabelStyle,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val textStyle = when (style) {
        RewardTypeLabelStyle.DEFAULT -> MaterialTheme.typography.titleMedium
        RewardTypeLabelStyle.SMALL -> MaterialTheme.typography.labelMedium
    }

    val iconSize = when (style) {
        RewardTypeLabelStyle.DEFAULT -> 24.dp
        RewardTypeLabelStyle.SMALL -> 20.dp
    }

    Crossfade(
        modifier = modifier,
        targetState = rewardType,
        label = "Reward Type Label Loading"
    ) {
        if (it == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(getScaledLineHeightFromFontStyle(density, configuration, textStyle))
                    .clip(MaterialTheme.shapes.small)
                    .pulseAnimation()
            )
        } else {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = it.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = it.displayName,
                    style = textStyle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RewardTypeLabelPreview() {
    ValolinkTheme {
        Surface {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = RewardType.entries, itemContent = {
                    RewardTypeLabel(
                        rewardType = it,
                        style = RewardTypeLabelStyle.DEFAULT
                    )
                })

                item {
                    RewardTypeLabel(
                        rewardType = null,
                        style = RewardTypeLabelStyle.DEFAULT
                    )
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SmallRewardTypeLabelPreview() {
    ValolinkTheme {
        Surface {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = RewardType.entries, itemContent = {
                    RewardTypeLabel(
                        rewardType = it,
                        style = RewardTypeLabelStyle.SMALL
                    )
                })

                item {
                    RewardTypeLabel(
                        rewardType = null,
                        style = RewardTypeLabelStyle.DEFAULT
                    )
                }
            }
        }
    }
}
/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LevelHeader.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.core.ui.components.rewardCard.RewardTypeLabel
import dev.bittim.valolink.core.ui.components.rewardCard.RewardTypeLabelStyle
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.extensions.modifier.conditional
import dev.bittim.valolink.core.ui.util.extensions.modifier.pulseAnimation
import dev.bittim.valolink.core.ui.util.getScaledLineHeightFromFontStyle

@Immutable
data class LevelHeaderData(
    val displayName: String,
    val type: RewardType,
    val displayIcon: String,
    val levelName: String,
    val contractName: String,
)

@Composable
fun LevelHeader(
    modifier: Modifier = Modifier,
    data: LevelHeaderData?,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.l)
        ) {
            Crossfade(
                modifier = Modifier.animateContentSize(),
                targetState = data,
                label = "Image loading crossfade"
            ) {
                if (it == null) {
                    Box(
                        modifier = Modifier
                            .height(96.dp)
                            .aspectRatio(1f)
                            .padding(1.dp)
                            .clip(MaterialTheme.shapes.large)
                            .pulseAnimation()
                    )
                } else {
                    Surface(
                        modifier = Modifier
                            .height(96.dp)
                            .aspectRatio(1f)
                            .clip(MaterialTheme.shapes.large),
                        tonalElevation = 3.dp
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .conditional(it.type != RewardType.PLAYER_CARD && it.type != RewardType.SPRAY && it.type != RewardType.FLEX) {
                                    padding(Spacing.s)
                                },
                            model = it.displayIcon,
                            contentScale = ContentScale.Fit,
                            colorFilter = if (it.type == RewardType.CURRENCY || it.type == RewardType.TITLE) ColorFilter.tint(
                                MaterialTheme.colorScheme.onSurface
                            ) else ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(1f) }),
                            contentDescription = it.displayName,
                            placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_card_displayicon)
                        )
                    }
                }
            }

            Column {
                Crossfade(
                    modifier = Modifier.animateContentSize(),
                    targetState = data,
                    label = "Level and contract name crossfade"
                ) {
                    if (it == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(
                                    getScaledLineHeightFromFontStyle(
                                        density,
                                        configuration,
                                        MaterialTheme.typography.labelLarge
                                    ) - Spacing.xxs // Spacing.xxs is the padding
                                )
                                .padding(1.dp)
                                .clip(MaterialTheme.shapes.small)
                                .pulseAnimation()
                        )
                    } else {
                        Text(
                            text = "${it.levelName} • ${it.contractName}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Crossfade(
                    modifier = Modifier.animateContentSize(),
                    targetState = data,
                    label = "Display name crossfade"
                ) {
                    if (it == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(
                                    getScaledLineHeightFromFontStyle(
                                        density,
                                        configuration,
                                        MaterialTheme.typography.titleLarge
                                    ) - Spacing.xxs // Spacing.xxs is the padding
                                )
                                .padding(1.dp)
                                .clip(MaterialTheme.shapes.small)
                                .pulseAnimation()
                        )
                    } else {
                        Text(
                            text = it.displayName,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.s))

                RewardTypeLabel(
                    modifier = Modifier.fillMaxWidth(),
                    rewardType = data?.type,
                    style = RewardTypeLabelStyle.DEFAULT
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LevelHeaderPreview() {
    ValolinkTheme {
        Surface {
            Box(
                modifier = Modifier
                    .width(412.dp)
                    .padding(Spacing.l),
            ) {
                LevelHeader(
                    data = LevelHeaderData(
                        displayName = "Metamorphosis Card",
                        type = RewardType.PLAYER_CARD,
                        displayIcon = "",
                        levelName = "Level 9",
                        contractName = "Clove Contract",
                    ),
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProgressLevelHeaderPreview() {
    ValolinkTheme {
        Surface {
            Box(
                modifier = Modifier
                    .width(412.dp)
                    .padding(Spacing.l),
            ) {
                LevelHeader(
                    data = LevelHeaderData(
                        displayName = "Metamorphosis Card",
                        type = RewardType.PLAYER_CARD,
                        displayIcon = "",
                        levelName = "Level 9",
                        contractName = "Clove Contract",
                    ),
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NullProgressLevelHeaderPreview() {
    ValolinkTheme {
        Surface {
            Box(
                modifier = Modifier
                    .width(412.dp)
                    .padding(Spacing.l),
            ) {
                LevelHeader(
                    data = null,
                )
            }
        }
    }
}

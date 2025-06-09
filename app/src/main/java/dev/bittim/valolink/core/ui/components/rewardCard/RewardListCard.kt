/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RewardListCard.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.core.ui.components.rewardCard

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.content.ui.screens.content.contracts.components.LevelBackdrop
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.extensions.modifier.conditional
import dev.bittim.valolink.core.ui.util.extensions.modifier.pulseAnimation
import dev.bittim.valolink.core.ui.util.getScaledLineHeightFromFontStyle

@Immutable
data class RewardListCardData(
    val name: String,
    val levelUuid: String,
    val type: RewardType,
    val levelName: String,
    val contractName: String,
    val rewardCount: Int,
    val amount: Int,
    val useXP: Boolean,
    val xpTotal: Int = 0,
    val displayIcon: String,
    val background: String? = null,
)

@Composable
fun RewardListCard(
    modifier: Modifier = Modifier,
    data: RewardListCardData?,
    xpCollected: Int = 0,
    isLocked: Boolean = false,
    isOwned: Boolean = false,
    onNavToLevelDetails: (levelUuid: String) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    var isOwned by remember { mutableStateOf(isOwned) }

    LaunchedEffect(xpCollected) {
        isOwned = data?.useXP == true && xpCollected >= data.xpTotal
    }

    Card(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clickable {
                if (data?.levelUuid != null) onNavToLevelDetails(data.levelUuid)
            }
    ) {
        Row {
            Surface(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium),
                tonalElevation = 3.dp
            ) {
                Crossfade(targetState = data, label = "Image Loading") {
                    if (it == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pulseAnimation()
                        )
                    } else {
                        if (it.type != RewardType.PLAYER_CARD) {
                            LevelBackdrop(
                                isDisabled = isLocked,
                                backgroundImage = it.background
                            ) {}
                        }

                        val imagePadding = when (it.type) {
                            RewardType.CURRENCY, RewardType.TITLE -> PaddingValues(Spacing.l)
                            RewardType.PLAYER_CARD, RewardType.SPRAY, RewardType.AGENT, RewardType.FLEX -> PaddingValues(
                                0.dp
                            )

                            else -> PaddingValues(Spacing.s)
                        }

                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(imagePadding)
                                .conditional(isLocked || isOwned) {
                                    blur(
                                        Spacing.xs,
                                        BlurredEdgeTreatment.Rectangle
                                    )
                                },
                            model = it.displayIcon,
                            contentDescription = null,
                            colorFilter = if (it.type == RewardType.CURRENCY || it.type == RewardType.TITLE) ColorFilter.tint(
                                MaterialTheme.colorScheme.onSurface
                            ) else ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(if (isLocked) 0.3f else 1f) }),
                            contentScale = if (it.type == RewardType.PLAYER_CARD) ContentScale.FillWidth else ContentScale.Fit,
                            alignment = if (it.type == RewardType.PLAYER_CARD) Alignment.TopCenter else Alignment.Center,
                            placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_card_displayicon)
                        )



                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLocked) {
                                Box(
                                    modifier = Modifier
                                        .height(66.dp)
                                        .aspectRatio(1f)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.radialGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.surfaceContainer,
                                                    Color.Transparent
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .height(Spacing.xxxl)
                                            .aspectRatio(1f),
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }


                            if (!isLocked && isOwned) {
                                Box(
                                    modifier = Modifier
                                        .height(66.dp)
                                        .aspectRatio(1f)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.radialGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.surfaceContainer,
                                                    Color.Transparent
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .height(Spacing.xxxl)
                                            .aspectRatio(1f),
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(Spacing.l))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        top = Spacing.l,
                        bottom = Spacing.l,
                        end = Spacing.l
                    ),
                verticalArrangement = Arrangement.Top
            ) {
                Crossfade(
                    targetState = data,
                    label = "Name Loading"
                ) {
                    if (it == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(
                                    getScaledLineHeightFromFontStyle(
                                        density,
                                        configuration,
                                        MaterialTheme.typography.labelMedium
                                    )
                                )
                                .clip(MaterialTheme.shapes.small)
                                .pulseAnimation()
                        )
                    } else {
                        Text(
                            text = "${it.levelName} • ${it.contractName}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.height(Spacing.xxs))

                Crossfade(
                    targetState = data,
                    label = "Name Loading"
                ) {
                    if (it == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(
                                    getScaledLineHeightFromFontStyle(
                                        density,
                                        configuration,
                                        MaterialTheme.typography.titleMedium
                                    )
                                )
                                .clip(MaterialTheme.shapes.small)
                                .pulseAnimation()
                        )
                    } else {
                        Text(
                            text = if (it.amount > 1) "${it.amount} ${it.name}" else it.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.height(Spacing.s))

                RewardTypeLabel(
                    modifier = Modifier.fillMaxWidth(),
                    rewardType = data?.type,
                    style = RewardTypeLabelStyle.SMALL
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Crossfade(
                    modifier = Modifier.padding(top = Spacing.s, end = Spacing.s),
                    targetState = data?.rewardCount,
                    label = "Reward count badge Loading"
                ) { count ->
                    if (count == null || count < 2) {
                        Spacer(modifier = Modifier.height(MaterialTheme.typography.titleSmall.lineHeight.value.dp * configuration.fontScale + Spacing.l))
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary)
                                .padding(horizontal = Spacing.l, vertical = Spacing.s)
                        ) {
                            Text(
                                text = "+${count - 1}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }

                Crossfade(
                    modifier = Modifier.padding(bottom = Spacing.s, end = Spacing.s),
                    targetState = data,
                    label = "XP Progress Loading"
                ) { checkedData ->
                    if (checkedData != null && checkedData.useXP == true) {
                        ProgressCluster(
                            modifier = Modifier.size(Spacing.xxl),
                            progress = xpCollected,
                            total = checkedData.xpTotal,
                            unit = UiText.StringResource(R.string.unit_xp).asString(),
                            isMonochrome = false,
                            isCompact = true
                        )
                    } else {
                        Spacer(modifier = Modifier.height(1.dp))
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun AgentRewardListCardPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            RewardListCard(
                modifier = Modifier.fillMaxWidth(),
                data = RewardListCardData(
                    name = "Metamorphosis Card",
                    levelUuid = "",
                    type = RewardType.PLAYER_CARD,
                    levelName = "Level 9",
                    contractName = "Clove Contract",
                    rewardCount = 3,
                    amount = 1,
                    useXP = true,
                    xpTotal = 10000,
                    displayIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                ),
                xpCollected = 3000,
                onNavToLevelDetails = {}
            )
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun LongNameAgentRewardListCardPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            RewardListCard(
                modifier = Modifier.fillMaxWidth(),
                data = RewardListCardData(
                    name = "Epilogue: Build Your Own Vandal Card",
                    levelUuid = "",
                    type = RewardType.PLAYER_CARD,
                    levelName = "Level 9",
                    contractName = "Clove Contract",
                    rewardCount = 3,
                    amount = 1,
                    useXP = false,
                    displayIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                ),
                onNavToLevelDetails = {}
            )
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun LockedAgentRewardListCardPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            RewardListCard(
                modifier = Modifier.fillMaxWidth(),
                data = RewardListCardData(
                    name = "Metamorphosis Card",
                    levelUuid = "",
                    type = RewardType.PLAYER_CARD,
                    levelName = "Level 9",
                    contractName = "Clove Contract",
                    rewardCount = 1,
                    amount = 1,
                    useXP = true,
                    xpTotal = 10000,
                    displayIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                ),
                xpCollected = 3000,
                isLocked = true,
                onNavToLevelDetails = {}
            )
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun OwnedAgentRewardListCardPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            RewardListCard(
                modifier = Modifier.fillMaxWidth(),
                data = RewardListCardData(
                    name = "Metamorphosis Card",
                    levelUuid = "",
                    type = RewardType.PLAYER_CARD,
                    levelName = "Level 9",
                    contractName = "Clove Contract",
                    rewardCount = 1,
                    amount = 1,
                    useXP = false,
                    displayIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                ),
                isOwned = true,
                onNavToLevelDetails = {}
            )
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun LoadingAgentRewardListCardPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            RewardListCard(
                modifier = Modifier.fillMaxWidth(),
                data = null,
                onNavToLevelDetails = {}
            )
        }
    }
}

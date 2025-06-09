/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RewardCard.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.core.ui.components.rewardCard

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.content.ui.screens.content.contracts.components.LevelBackdrop
import dev.bittim.valolink.core.ui.components.OrientableContainer
import dev.bittim.valolink.core.ui.components.UnlockButton
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ComponentPreviewAnnotations
import dev.bittim.valolink.core.ui.util.extensions.modifier.conditional
import dev.bittim.valolink.core.ui.util.extensions.modifier.pulseAnimation
import dev.bittim.valolink.core.ui.util.getScaledLineHeightFromFontStyle

data object RewardCard {
    val width: Dp = 256.dp
    val minHeight: Dp = 118.dp
    val maxHeight: Dp = 308.dp
}

@Immutable
data class RewardCardData(
    val name: String,
    val levelUuid: String,
    val type: RewardType,
    val levelName: String,
    val contractName: String,
    val rewardCount: Int,
    val useXP: Boolean,
    val xpTotal: Int = 0,
    val price: Int,
    val amount: Int,
    val previewIcon: String,
    val currencyIcon: String,
    val background: String? = null,
)

@Composable
fun RewardCard(
    modifier: Modifier = Modifier,
    data: RewardCardData?,
    xpCollected: Int = 0,
    isLocked: Boolean = false,
    isOwned: Boolean = false,
    unlockReward: () -> Unit = {},
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
            .width(RewardCard.width)
            .heightIn(RewardCard.minHeight, RewardCard.maxHeight)
            .clickable { if (data?.levelUuid != null) onNavToLevelDetails(data.levelUuid) },
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .clip(MaterialTheme.shapes.medium),
            tonalElevation = 3.dp
        ) {
            Crossfade(targetState = data, label = "Image Loading") { data ->
                if (data == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pulseAnimation()
                    )
                } else {
                    if (data.type != RewardType.PLAYER_CARD) {
                        LevelBackdrop(
                            isDisabled = isLocked,
                            backgroundImage = data.background
                        ) {}
                    }

                    val imagePadding = when (data.type) {
                        RewardType.CURRENCY, RewardType.TITLE -> PaddingValues(Spacing.xxl)
                        RewardType.PLAYER_CARD, RewardType.FLEX -> PaddingValues(0.dp)
                        else -> PaddingValues(Spacing.l)
                    }

                    Crossfade(
                        targetState = isLocked || isOwned,
                        label = "Locked overlay Loading"
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(imagePadding)
                                .conditional(it) {
                                    Modifier.blur(
                                        Spacing.xs,
                                        BlurredEdgeTreatment.Rectangle
                                    )
                                },
                            model = data.previewIcon,
                            contentDescription = null,
                            colorFilter = if (data.type == RewardType.CURRENCY || data.type == RewardType.TITLE) ColorFilter.tint(
                                MaterialTheme.colorScheme.onSurface
                            ) else ColorFilter.colorMatrix(ColorMatrix().apply {
                                setToSaturation(
                                    if (isLocked) 0.3f else 1f
                                )
                            }),
                            contentScale = if (data.type == RewardType.PLAYER_CARD) ContentScale.FillWidth else ContentScale.Fit,
                            alignment = if (data.type == RewardType.PLAYER_CARD) Alignment.TopCenter else Alignment.Center,
                            placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_card_largeart)
                        )
                    }



                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Crossfade(
                            targetState = isLocked,
                            label = "Locked overlay Loading"
                        ) {
                            if (it) {
                                Box(
                                    modifier = Modifier
                                        .height(88.dp)
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
                                            .height(64.dp)
                                            .aspectRatio(1f),
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Crossfade(
                            targetState = isOwned && !isLocked,
                            label = "Owned overlay Loading"
                        ) {
                            if (it) {
                                Box(
                                    modifier = Modifier
                                        .height(88.dp)
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
                                            .height(64.dp)
                                            .aspectRatio(1f),
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Crossfade(
                                    targetState = data?.rewardCount,
                                    label = "Reward count badge Loading"
                                ) { count ->
                                    if (count == null || count < 2) {
                                        Spacer(Modifier.width(1.dp))
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .padding(Spacing.m)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.secondary)
                                                .padding(
                                                    horizontal = Spacing.l,
                                                    vertical = Spacing.s
                                                )
                                        ) {
                                            Text(
                                                text = "+${count - 1}",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSecondary
                                            )
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .padding(Spacing.m)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondary)
                                        .padding(
                                            horizontal = Spacing.l,
                                            vertical = Spacing.s
                                        )
                                ) {
                                    RewardTypeLabel(
                                        rewardType = data?.type,
                                        style = RewardTypeLabelStyle.SMALL,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing.l))

        Column(
            modifier = Modifier
                .padding(
                    start = Spacing.l,
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

            Crossfade(
                targetState = data,
                label = "Unlock button Loading"
            ) {
                if (it == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Spacing.xs)
                            .height(40.dp)
                            .clip(ButtonDefaults.shape)
                            .pulseAnimation()
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (it.useXP) {
                            ProgressCluster(
                                modifier = Modifier
                                    .padding(vertical = Spacing.xs)
                                    .height(40.dp),
                                total = it.xpTotal,
                                progress = xpCollected,
                                unit = UiText.StringResource(R.string.unit_xp).asString(),
                                isMonochrome = false,
                                isCompact = false
                            )
                        } else {
                            UnlockButton(
                                currencyIcon = it.currencyIcon,
                                price = it.price,
                                isPrimary = true,
                                isLocked = isLocked,
                                isOwned = isOwned,
                                onClick = unlockReward
                            )
                        }
                    }
                }
            }
        }
    }
}

@ComponentPreviewAnnotations
@Composable
fun RewardCardPreview() {
    ValolinkTheme {
        Surface {
            OrientableContainer(
                portraitContainer = { modifier, content ->
                    Column(
                        modifier = modifier,
                        verticalArrangement = Arrangement.spacedBy(Spacing.s)
                    ) { content() }
                },
                landscapeContainer = { modifier, content ->
                    Row(
                        modifier = modifier,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.s)
                    ) { content() }
                }
            ) {
                RewardCard(
                    data = RewardCardData(
                        name = "Metamorphosis Card",
                        levelUuid = "",
                        type = RewardType.PLAYER_CARD,
                        levelName = "Level 9",
                        contractName = "Clove Contract",
                        rewardCount = 3,
                        useXP = false,
                        price = 2000,
                        amount = 1,
                        previewIcon = "",
                        currencyIcon = "",
                        background = "",
                    ),
                    unlockReward = {},
                    onNavToLevelDetails = {}
                )

                RewardCard(
                    data = RewardCardData(
                        name = "Metamorphosis Card",
                        levelUuid = "",
                        type = RewardType.PLAYER_CARD,
                        levelName = "Level 9",
                        contractName = "Clove Contract",
                        rewardCount = 3,
                        useXP = true,
                        xpTotal = 10000,
                        price = 2000,
                        amount = 1,
                        previewIcon = "",
                        currencyIcon = "",
                        background = "",
                    ),
                    xpCollected = 3000,
                    unlockReward = {},
                    onNavToLevelDetails = {}
                )
            }
        }
    }
}

@ComponentPreviewAnnotations
@Composable
fun LockedRewardCardPreview() {
    ValolinkTheme {
        Surface {
            OrientableContainer(
                portraitContainer = { modifier, content ->
                    Column(
                        modifier = modifier,
                        verticalArrangement = Arrangement.spacedBy(Spacing.s)
                    ) { content() }
                },
                landscapeContainer = { modifier, content ->
                    Row(
                        modifier = modifier,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.s)
                    ) { content() }
                }
            ) {
                RewardCard(
                    data = RewardCardData(
                        name = "Metamorphosis Card",
                        levelUuid = "",
                        type = RewardType.PLAYER_CARD,
                        levelName = "Level 9",
                        contractName = "Clove Contract",
                        rewardCount = 3,
                        useXP = false,
                        xpTotal = 0,
                        price = 2000,
                        amount = 1,
                        previewIcon = "",
                        currencyIcon = "",
                        background = "",
                    ),
                    isLocked = true,
                    unlockReward = {},
                    onNavToLevelDetails = {}
                )

                RewardCard(
                    data = RewardCardData(
                        name = "Metamorphosis Card",
                        levelUuid = "",
                        type = RewardType.PLAYER_CARD,
                        levelName = "Level 9",
                        contractName = "Clove Contract",
                        rewardCount = 3,
                        useXP = true,
                        xpTotal = 10000,
                        price = 2000,
                        amount = 1,
                        previewIcon = "",
                        currencyIcon = "",
                        background = "",
                    ),
                    xpCollected = 3000,
                    isLocked = true,
                    unlockReward = {},
                    onNavToLevelDetails = {}
                )
            }
        }
    }
}

@ComponentPreviewAnnotations
@Composable
fun OwnedRewardCardPreview() {
    ValolinkTheme {
        Surface {
            OrientableContainer(
                portraitContainer = { modifier, content ->
                    Column(
                        modifier = modifier,
                        verticalArrangement = Arrangement.spacedBy(Spacing.s)
                    ) { content() }
                },
                landscapeContainer = { modifier, content ->
                    Row(
                        modifier = modifier,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.s)
                    ) { content() }
                }
            ) {
                RewardCard(
                    data = RewardCardData(
                        name = "Metamorphosis Card",
                        levelUuid = "",
                        type = RewardType.PLAYER_CARD,
                        levelName = "Level 9",
                        contractName = "Clove Contract",
                        rewardCount = 3,
                        useXP = false,
                        xpTotal = 0,
                        price = 2000,
                        amount = 1,
                        previewIcon = "",
                        currencyIcon = "",
                        background = "",
                    ),
                    isOwned = true,
                    unlockReward = {},
                    onNavToLevelDetails = {}
                )

                RewardCard(
                    data = RewardCardData(
                        name = "Metamorphosis Card",
                        levelUuid = "",
                        type = RewardType.PLAYER_CARD,
                        levelName = "Level 9",
                        contractName = "Clove Contract",
                        rewardCount = 3,
                        useXP = true,
                        xpTotal = 10000,
                        price = 2000,
                        amount = 1,
                        previewIcon = "",
                        currencyIcon = "",
                        background = "",
                    ),
                    xpCollected = 3000,
                    isOwned = true,
                    unlockReward = {},
                    onNavToLevelDetails = {}
                )
            }
        }
    }
}

@ComponentPreviewAnnotations
@Composable
fun LoadingRewardCardPreview() {
    ValolinkTheme {
        Surface {
            RewardCard(
                data = null,
                unlockReward = {},
                onNavToLevelDetails = {}
            )
        }
    }
}

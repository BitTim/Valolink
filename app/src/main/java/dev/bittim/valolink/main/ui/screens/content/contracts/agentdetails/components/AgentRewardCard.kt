package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.components

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardType
import dev.bittim.valolink.main.ui.components.RewardTypeLabel
import dev.bittim.valolink.main.ui.components.RewardTypeLabelStyle
import dev.bittim.valolink.main.ui.components.UnlockButton
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.components.conditional
import dev.bittim.valolink.main.ui.components.pulseAnimation
import dev.bittim.valolink.main.ui.screens.content.contracts.components.LevelBackdrop
import dev.bittim.valolink.main.ui.util.getScaledLineHeightFromFontStyle

data object AgentRewardCard {
    val width: Dp = 256.dp
}

@Immutable
data class AgentRewardCardData(
    val name: String,
    val levelUuid: String,
    val type: RewardType,
    val levelName: String,
    val contractName: String,
    val rewardCount: Int,
    val price: Int,
    val amount: Int,
    val previewIcon: String,
    val currencyIcon: String,
    val background: String? = null,
    val isLocked: Boolean = false,
    val isOwned: Boolean = false,
)

@Composable
fun AgentRewardCard(
    modifier: Modifier = Modifier,
    data: AgentRewardCardData?,
    unlockReward: () -> Unit = {},
    resetReward: () -> Unit = {},
    onNavToLevelDetails: (levelUuid: String) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    Card(
        modifier = modifier
            .width(AgentRewardCard.width)
            .clickable { if (data?.levelUuid != null) onNavToLevelDetails(data.levelUuid) }
    ) {
        var isMenuExpanded: Boolean by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier
                .aspectRatio(4f / 3f)
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
                            isDisabled = it.isLocked,
                            backgroundImage = it.background
                        ) {}
                    }

                    val imagePadding = when (it.type) {
                        RewardType.CURRENCY, RewardType.TITLE -> PaddingValues(32.dp)
                        RewardType.PLAYER_CARD                -> PaddingValues(0.dp)
                        else                                  -> PaddingValues(16.dp)
                    }

                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(imagePadding)
                            .conditional(it.isLocked || it.isOwned) {
                                blur(
                                    4.dp,
                                    BlurredEdgeTreatment.Rectangle
                                )
                            },
                        model = it.previewIcon,
                        contentDescription = null,
                        colorFilter = if (it.type == RewardType.CURRENCY || it.type == RewardType.TITLE) ColorFilter.tint(
                            MaterialTheme.colorScheme.onSurface
                        ) else ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(if (it.isLocked) 0.3f else 1f) }),
                        contentScale = if (it.type == RewardType.PLAYER_CARD) ContentScale.FillWidth else ContentScale.Fit,
                        alignment = if (it.type == RewardType.PLAYER_CARD) Alignment.TopCenter else Alignment.Center,
                        placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_card_largeart)
                    )



                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
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
                                                .padding(12.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.secondary)
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            Text(
                                                text = "+${count - 1}",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSecondary
                                            )
                                        }
                                    }
                                }

                                Box {
                                    FilledTonalIconButton(
                                        modifier = Modifier.padding(8.dp),
                                        onClick = { isMenuExpanded = true },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = null
                                        )
                                    }

                                    DropdownMenu(expanded = isMenuExpanded,
                                        onDismissRequest = { isMenuExpanded = false }) {
                                        DropdownMenuItem(enabled = data?.isLocked == false && data.isOwned,
                                            text = { Text(text = "Reset") },
                                            onClick = {
                                                resetReward()
                                                isMenuExpanded = false
                                            })
                                    }
                                }
                            }
                        }

                        if (it.isLocked) {
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


                        if (!it.isLocked && it.isOwned) {
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
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    bottom = 16.dp,
                    end = 16.dp
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

            Spacer(Modifier.height(2.dp))

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

            Spacer(Modifier.height(8.dp))

            RewardTypeLabel(
                modifier = Modifier.fillMaxWidth(),
                rewardType = data?.type,
                style = RewardTypeLabelStyle.SMALL
            )

            Spacer(Modifier.height(8.dp))

            Crossfade(
                targetState = data,
                label = "Unlock button Loading"
            ) {
                if (it == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(ButtonDefaults.MinHeight * configuration.fontScale)
                            .padding(1.dp)
                            .clip(ButtonDefaults.shape)
                            .pulseAnimation()
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        UnlockButton(
                            currencyIcon = it.currencyIcon,
                            price = it.price,
                            isPrimary = true,
                            isLocked = it.isLocked,
                            isOwned = it.isOwned,
                            onClick = unlockReward
                        )
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
fun AgentRewardCardPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            AgentRewardCard(
                data = AgentRewardCardData(
                    name = "Metamorphosis Card",
                    levelUuid = "",
                    type = RewardType.PLAYER_CARD,
                    levelName = "Level 9",
                    contractName = "Clove Contract",
                    rewardCount = 3,
                    price = 2000,
                    amount = 1,
                    previewIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                    currencyIcon = "https://media.valorant-api.com/currencies/85ca954a-41f2-ce94-9b45-8ca3dd39a00d/displayicon.png"
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
fun LongNameAgentRewardCardPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            AgentRewardCard(
                data = AgentRewardCardData(
                    name = "Epilogue: Build Your Own Vandal Card",
                    levelUuid = "",
                    type = RewardType.PLAYER_CARD,
                    levelName = "Level 9",
                    contractName = "Clove Contract",
                    rewardCount = 1,
                    price = 2000,
                    amount = 1,
                    previewIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                    currencyIcon = "https://media.valorant-api.com/currencies/85ca954a-41f2-ce94-9b45-8ca3dd39a00d/displayicon.png"
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
fun LockedAgentRewardCardPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            AgentRewardCard(
                data = AgentRewardCardData(
                    name = "Metamorphosis Card",
                    levelUuid = "",
                    type = RewardType.PLAYER_CARD,
                    levelName = "Level 9",
                    contractName = "Clove Contract",
                    rewardCount = 1,
                    price = 2000,
                    amount = 1,
                    previewIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                    currencyIcon = "https://media.valorant-api.com/currencies/85ca954a-41f2-ce94-9b45-8ca3dd39a00d/displayicon.png",
                    isLocked = true
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
fun OwnedAgentRewardCardPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            AgentRewardCard(
                data = AgentRewardCardData(
                    name = "Metamorphosis Card",
                    levelUuid = "",
                    type = RewardType.PLAYER_CARD,
                    levelName = "Level 9",
                    contractName = "Clove Contract",
                    rewardCount = 1,
                    price = 2000,
                    amount = 1,
                    previewIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                    currencyIcon = "https://media.valorant-api.com/currencies/85ca954a-41f2-ce94-9b45-8ca3dd39a00d/displayicon.png",
                    isOwned = true
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
fun LoadingAgentRewardCardPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            AgentRewardCard(
                data = null,
                onNavToLevelDetails = {}
            )
        }
    }
}
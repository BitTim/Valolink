package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.components

import android.content.res.Configuration
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardType
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.components.conditional
import dev.bittim.valolink.main.ui.screens.content.contracts.components.LevelBackdrop

data object AgentRewardCard {
    val width: Dp = 256.dp
}

// TODO: Rework these a bit
@Composable
fun AgentRewardCard(
    modifier: Modifier = Modifier,
    name: String,
    levelUuid: String,
    type: RewardType,
    price: Int,
    amount: Int,
    previewIcon: String,
    currencyIcon: String,
    displayIcon: String? = null,
    isLocked: Boolean = false,
    isOwned: Boolean = false,
    unlockReward: () -> Unit = {},
    resetReward: () -> Unit = {},
    onNavToLevelDetails: (String) -> Unit,
) {
    Card(
        modifier = modifier
            .width(AgentRewardCard.width)
            .aspectRatio(0.8f)
            .fillMaxSize()
            .clickable { onNavToLevelDetails(levelUuid) }
    ) {
        val imagePadding = when (type) {
            RewardType.CURRENCY, RewardType.TITLE -> PaddingValues(32.dp)
            RewardType.PLAYER_CARD                -> PaddingValues(0.dp)
            else                                  -> PaddingValues(16.dp)
        }

        var isMenuExpanded: Boolean by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.8f)
                .clip(MaterialTheme.shapes.medium),
            tonalElevation = 3.dp
        ) {
            if (type != RewardType.PLAYER_CARD) {
                LevelBackdrop(isDisabled = isLocked, backgroundImage = displayIcon) {

                }
            }

            AsyncImage(
                modifier = Modifier
                    .padding(imagePadding)
                    .conditional(isLocked || isOwned) {
                        blur(
                            4.dp,
                            BlurredEdgeTreatment.Rectangle
                        )
                    },
                model = previewIcon,
                contentDescription = null,
                colorFilter = if (type == RewardType.CURRENCY || type == RewardType.TITLE) ColorFilter.tint(
                    MaterialTheme.colorScheme.onSurface
                ) else ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(if (isLocked) 0.3f else 1f) }),
                contentScale = if (type == RewardType.PLAYER_CARD) ContentScale.FillWidth else ContentScale.Fit,
                alignment = if (type == RewardType.PLAYER_CARD) Alignment.TopCenter else Alignment.Center,
                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_card_largeart)
            )


            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopEnd
                ) {
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
                            DropdownMenuItem(enabled = !isLocked && isOwned,
                                             text = { Text(text = "Reset") },
                                             onClick = {
                                                 resetReward()
                                                 isMenuExpanded = false
                                             })
                        }
                    }
                }

                if (isLocked) {
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

                if (isOwned && !isLocked) {
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

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(
                    start = 16.dp,
                    bottom = 16.dp,
                    end = 16.dp
                ),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = if (amount > 1) "$amount $name" else name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = type.displayName,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = unlockReward,
                    enabled = !isLocked && !isOwned
                ) {
                    if (!isLocked && !isOwned) {
                        AsyncImage(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .width(16.dp)
                                .aspectRatio(1f),
                            model = currencyIcon,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                            placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_kingdom_kreds)
                        )
                    }

                    val buttonText =
                        if (!isLocked && !isOwned) "$price" else if (isLocked) "Locked" else "Owned"

                    Text(
                        text = buttonText,
                    )
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
                name = "Metamorphosis Card",
                levelUuid = "",
                type = RewardType.PLAYER_CARD,
                price = 2000,
                amount = 1,
                previewIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                currencyIcon = "https://media.valorant-api.com/currencies/85ca954a-41f2-ce94-9b45-8ca3dd39a00d/displayicon.png",
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
                name = "Metamorphosis Card",
                levelUuid = "",
                type = RewardType.PLAYER_CARD,
                price = 2000,
                amount = 1,
                previewIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                currencyIcon = "https://media.valorant-api.com/currencies/85ca954a-41f2-ce94-9b45-8ca3dd39a00d/displayicon.png",
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
fun OwnedAgentRewardCardPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            AgentRewardCard(
                name = "Metamorphosis Card",
                levelUuid = "",
                type = RewardType.PLAYER_CARD,
                price = 2000,
                amount = 1,
                previewIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                currencyIcon = "https://media.valorant-api.com/currencies/85ca954a-41f2-ce94-9b45-8ca3dd39a00d/displayicon.png",
                isOwned = true,
                onNavToLevelDetails = {}
            )
        }
    }
}
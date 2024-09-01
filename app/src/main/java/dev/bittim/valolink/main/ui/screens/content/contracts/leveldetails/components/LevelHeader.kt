package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
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
import androidx.compose.material3.ButtonDefaults
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
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardType
import dev.bittim.valolink.main.ui.components.ProgressCluster
import dev.bittim.valolink.main.ui.components.RewardTypeLabel
import dev.bittim.valolink.main.ui.components.RewardTypeLabelStyle
import dev.bittim.valolink.main.ui.components.UnlockButton
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.components.conditional
import dev.bittim.valolink.main.ui.components.pulseAnimation
import dev.bittim.valolink.main.ui.util.getScaledLineHeightFromFontStyle

@Immutable
data class LevelHeaderData(
    val displayName: String,
    val type: RewardType,
    val displayIcon: String,
    val levelName: String,
    val contractName: String,
    val price: Int,
    val currencyIcon: String?,
    val xpTotal: Int,
    val xpProgress: Int = 0,
    val isLocked: Boolean,
    val isOwned: Boolean,
)

@Composable
fun LevelHeader(
    modifier: Modifier = Modifier,
    data: LevelHeaderData?,
    onUnlock: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Crossfade(targetState = data, label = "Image loading crossfade") {
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
                                .conditional(it.type != RewardType.PLAYER_CARD && it.type != RewardType.SPRAY) {
                                    padding(8.dp)
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
                                    ) - 2.dp // 2.dp is the padding
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
                                    ) - 2.dp // 2.dp is the padding
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

                Spacer(modifier = Modifier.height(8.dp))

                RewardTypeLabel(
                    modifier = Modifier.fillMaxWidth(),
                    rewardType = data?.type,
                    style = RewardTypeLabelStyle.DEFAULT
                )
            }
        }

        Crossfade(
            targetState = data,
            label = "Progress cluster crossfade"
        ) {
            if (it == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ButtonDefaults.MinHeight * configuration.fontScale)
                        .padding(1.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .pulseAnimation()
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (it.xpTotal >= 0) {
                        ProgressCluster(
                            modifier = Modifier.weight(2f),
                            progress = it.xpProgress,
                            total = it.xpTotal,
                            unit = "XP",
                            isMonochrome = false
                        )

                        Spacer(modifier = Modifier.width(24.dp))

                        if (it.currencyIcon != null) {
                            UnlockButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1.25f),
                                currencyIcon = it.currencyIcon,
                                price = it.price,
                                isPrimary = false,
                                isLocked = it.isLocked,
                                isOwned = it.isOwned,
                                onClick = onUnlock
                            )
                        }
                    } else {
                        if (it.currencyIcon != null) {
                            UnlockButton(
                                modifier = Modifier.fillMaxWidth(),
                                currencyIcon = it.currencyIcon,
                                price = it.price,
                                isPrimary = true,
                                isLocked = it.isLocked,
                                isOwned = it.isOwned,
                                onClick = onUnlock
                            )
                        }
                    }
                }
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
                    .padding(16.dp),
            ) {
                LevelHeader(
                    data = LevelHeaderData(
                        displayName = "Metamorphosis Card",
                        type = RewardType.PLAYER_CARD,
                        displayIcon = "",
                        levelName = "Level 9",
                        contractName = "Clove Contract",
                        price = 7500,
                        currencyIcon = "",
                        xpTotal = -1,
                        isLocked = false,
                        isOwned = false
                    ),
                    onUnlock = {}
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
                    .padding(16.dp),
            ) {
                LevelHeader(
                    data = LevelHeaderData(
                        displayName = "Metamorphosis Card",
                        type = RewardType.PLAYER_CARD,
                        displayIcon = "",
                        levelName = "Level 9",
                        contractName = "Clove Contract",
                        price = 99999,
                        currencyIcon = "",
                        xpProgress = 65,
                        xpTotal = 100,
                        isLocked = false,
                        isOwned = true
                    ),
                    onUnlock = {}
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
                    .padding(16.dp),
            ) {
                LevelHeader(
                    data = null,
                    onUnlock = {}
                )
            }
        }
    }
}
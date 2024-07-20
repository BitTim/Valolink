package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components

import android.content.res.Configuration
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardType
import dev.bittim.valolink.main.ui.components.ProgressCluster
import dev.bittim.valolink.main.ui.components.UnlockButton
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.components.conditional

@Composable
fun LevelHeader(
    modifier: Modifier = Modifier,
    displayName: String,
    type: RewardType,
    displayIcon: String,
    levelName: String,
    contractName: String,
    price: Int,
    currencyIcon: String,
    xpTotal: Int,
    xpProgress: Int = 0,
    isUnlocked: Boolean = false,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                        .conditional(type != RewardType.PLAYER_CARD && type != RewardType.SPRAY) {
                            padding(8.dp)
                        },
                    model = displayIcon,
                    contentScale = ContentScale.Fit,
                    colorFilter = if (type == RewardType.CURRENCY || type == RewardType.TITLE) ColorFilter.tint(
                        MaterialTheme.colorScheme.onSurface
                    ) else ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(1f) }),
                    contentDescription = displayName,
                    placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_card_displayicon)
                )
            }

            Column {
                Text(
                    text = "$levelName • $contractName",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = type.icon,
                        contentDescription = type.displayName,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = type.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (xpTotal >= 0) {
                ProgressCluster(
                    modifier = Modifier.weight(2f),
                    progress = xpProgress,
                    total = xpTotal,
                    unit = "XP",
                    isMonochrome = false
                )

                Spacer(modifier = Modifier.width(24.dp))

                UnlockButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.25f),
                    currencyIcon = currencyIcon,
                    price = price,
                    isPrimary = false,
                    isUnlocked = isUnlocked
                )
            } else {
                UnlockButton(
                    modifier = Modifier.fillMaxWidth(),
                    currencyIcon = currencyIcon,
                    price = price,
                    isPrimary = true,
                    isUnlocked = isUnlocked
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
                    .padding(16.dp),
            ) {
                LevelHeader(
                    displayName = "Metamorphosis Card",
                    type = RewardType.PLAYER_CARD,
                    displayIcon = "",
                    levelName = "Level 9",
                    contractName = "Clove Contract",
                    price = 7500,
                    currencyIcon = "",
                    xpTotal = -1,
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
                    displayName = "Metamorphosis Card",
                    type = RewardType.PLAYER_CARD,
                    displayIcon = "",
                    levelName = "Level 9",
                    contractName = "Clove Contract",
                    price = 99999,
                    currencyIcon = "",
                    xpProgress = 65,
                    xpTotal = 100,
                    isUnlocked = true
                )
            }
        }
    }
}
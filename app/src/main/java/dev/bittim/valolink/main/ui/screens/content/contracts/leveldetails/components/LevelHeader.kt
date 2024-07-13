package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardType
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.components.conditional

@Composable
fun LevelHeader(
    modifier: Modifier = Modifier,
    displayName: String,
    type: RewardType,
    displayIcon: String,
) {
    Row(
        modifier = modifier,
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
                contentDescription = displayName,
                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_card_displayicon)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

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
}
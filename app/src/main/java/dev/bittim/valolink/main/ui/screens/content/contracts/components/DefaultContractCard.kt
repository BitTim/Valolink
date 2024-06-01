package dev.bittim.valolink.main.ui.screens.content.contracts.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import java.util.UUID

@Composable
fun DefaultContractCard(
    modifier: Modifier = Modifier,
    contractUuid: String,
    displayName: String,
    displayIcon: String?,
    backgroundImage: String?,
    backgroundGradientColors: List<String>,
    remainingDays: Int?,
    collectedXp: Int,
    totalXp: Int,
    percentage: Int,
    onNavToContractDetails: (String) -> Unit,
) {
    AgentCardBase(
        modifier = modifier.clickable { onNavToContractDetails(contractUuid) },
        backgroundGradientColors = backgroundGradientColors,
        backgroundImage = backgroundImage
    ) { useGradient ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (displayIcon != null) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    model = displayIcon,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    placeholder = coilDebugPlaceholder(R.drawable.debug_agent_display_icon)
                )
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleLarge
                    )

                    if (remainingDays != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "$remainingDays days",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (useGradient) Color.White.copy(alpha = 0.65f) else MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                tint = if (useGradient) Color.White.copy(alpha = 0.65f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = null
                            )
                        }
                    }
                }

                val animatedProgress by animateFloatAsState(
                    targetValue = (percentage / 100f),
                    animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                    label = ""
                )

                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(),
                                        color = if (useGradient) Color.White else ProgressIndicatorDefaults.linearColor,
                                        trackColor = if (useGradient) Color.Black.copy(alpha = 0.3f) else ProgressIndicatorDefaults.linearTrackColor,
                                        progress = { animatedProgress })

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$collectedXp / $totalXp XP",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = "$percentage %",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultAgentContractCardPreview() {
    ValolinkTheme {
        DefaultContractCard(displayName = "Clove",
                            contractUuid = UUID.randomUUID().toString(),
                            displayIcon = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/displayicon.png",
                            backgroundImage = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/background.png",
                            backgroundGradientColors = listOf(
                                "f17cadff",
                                "062261ff",
                                "c347c7ff",
                                "f1db6fff"
                            ),
                            remainingDays = null,
                            collectedXp = 0,
                            totalXp = 200000,
                            percentage = 0,
                            onNavToContractDetails = {})
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultAgentContractCardWithTimePreview() {
    ValolinkTheme {
        DefaultContractCard(displayName = "Clove",
                            contractUuid = UUID.randomUUID().toString(),
                            displayIcon = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/displayicon.png",
                            backgroundImage = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/background.png",
                            backgroundGradientColors = listOf(
                                "f17cadff",
                                "062261ff",
                                "c347c7ff",
                                "f1db6fff"
                            ),
                            remainingDays = 5,
                            collectedXp = 148660,
                            totalXp = 200000,
                            percentage = 74,
                            onNavToContractDetails = {})
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultContractCardPreview() {
    ValolinkTheme {
        DefaultContractCard(displayName = "Ignition: Act I",
                            contractUuid = UUID.randomUUID().toString(),
                            displayIcon = null,
                            backgroundImage = null,
                            backgroundGradientColors = listOf(),
                            remainingDays = null,
                            collectedXp = 0,
                            totalXp = 0,
                            percentage = 0,
                            onNavToContractDetails = {})
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultContractCardWithTimePreview() {
    ValolinkTheme {
        DefaultContractCard(displayName = "Ignition: Act I",
                            contractUuid = UUID.randomUUID().toString(),
                            displayIcon = null,
                            backgroundImage = null,
                            backgroundGradientColors = listOf(),
                            remainingDays = 5,
                            collectedXp = 148660,
                            totalXp = 200000,
                            percentage = 74,
                            onNavToContractDetails = {})
    }
}
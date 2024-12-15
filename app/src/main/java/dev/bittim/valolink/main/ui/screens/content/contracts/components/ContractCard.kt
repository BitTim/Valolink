/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ContractCard.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.main.ui.screens.content.contracts.components

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.components.pulseAnimation
import java.util.UUID

@Immutable
data class ContractCardData(
    val contractUuid: String,
    val displayName: String,
    val displayIcon: String?,
    val backgroundImage: String?,
    val backgroundGradientColors: List<String>,
    val remainingDays: Int?,
    val collectedXp: Int,
    val totalXp: Int,
    val percentage: Int,
)

@Composable
fun ContractCard(
    modifier: Modifier = Modifier,
    data: ContractCardData?,
    onNavToContractDetails: (String) -> Unit,
) {
    AgentCardBase(
        modifier = modifier.clickable {
            if (data != null) onNavToContractDetails(data.contractUuid)
        },
        backgroundGradientColors = data?.backgroundGradientColors ?: emptyList(),
        backgroundImage = data?.backgroundImage
    ) { useGradient ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (data?.displayIcon != null) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    model = data.displayIcon,
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
                    Crossfade(
                        targetState = data,
                        label = "Contract name loading"
                    ) {
                        if (it == null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.65f)
                                    .height(MaterialTheme.typography.titleLarge.lineHeight.value.dp)
                                    .padding(1.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .pulseAnimation()
                            )
                        } else {
                            Text(
                                text = it.displayName,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }


                    Crossfade(
                        targetState = data,
                        label = "Remaining days loading"
                    ) {
                        if (it == null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.75f)
                                    .height(MaterialTheme.typography.titleLarge.lineHeight.value.dp)
                                    .padding(1.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .pulseAnimation()
                            )
                        } else {
                            if (it.remainingDays != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "${it.remainingDays} days",
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
                    }
                }

                Crossfade(
                    targetState = data,
                    label = "Progress loading"
                ) {
                    if (it != null) {
                        val animatedProgress by animateFloatAsState(
                            targetValue = (it.percentage / 100f),
                            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                            label = ""
                        )

                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = if (useGradient) Color.White else ProgressIndicatorDefaults.linearColor,
                            trackColor = if (useGradient) Color.Black.copy(alpha = 0.3f) else ProgressIndicatorDefaults.linearTrackColor,
                            progress = { animatedProgress }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Crossfade(
                        targetState = data,
                        label = "Progress loading"
                    ) {
                        if (it == null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .height(MaterialTheme.typography.bodyMedium.lineHeight.value.dp)
                                    .padding(1.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .pulseAnimation()
                            )
                        } else {
                            Text(
                                text = "${it.collectedXp} / ${it.totalXp} XP",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }

                    Crossfade(
                        targetState = data,
                        label = "Progress loading"
                    ) {
                        if (it == null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.75f)
                                    .height(MaterialTheme.typography.bodyMedium.lineHeight.value.dp)
                                    .padding(1.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .pulseAnimation()
                            )
                        } else {
                            Text(
                                text = "${it.percentage} %",
                                style = MaterialTheme.typography.bodyMedium,
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
fun DefaultAgentContractCardPreview() {
    ValolinkTheme {
        ContractCard(
            data = ContractCardData(
                displayName = "Clove",
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
                percentage = 0
            ),
            onNavToContractDetails = { }
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultAgentContractCardWithTimePreview() {
    ValolinkTheme {
        ContractCard(
            data = ContractCardData(
                displayName = "Clove",
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
                percentage = 74
            ),
            onNavToContractDetails = { }
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultContractCardPreview() {
    ValolinkTheme {
        ContractCard(
            data = ContractCardData(
                displayName = "Ignition: Act I",
                contractUuid = UUID.randomUUID().toString(),
                displayIcon = null,
                backgroundImage = null,
                backgroundGradientColors = emptyList(),
                remainingDays = null,
                collectedXp = 0,
                totalXp = 0,
                percentage = 0
            ),
            onNavToContractDetails = { }
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultContractCardWithTimePreview() {
    ValolinkTheme {
        ContractCard(
            data = ContractCardData(
                displayName = "Ignition: Act I",
                contractUuid = UUID.randomUUID().toString(),
                displayIcon = null,
                backgroundImage = null,
                backgroundGradientColors = emptyList(),
                remainingDays = 5,
                collectedXp = 148660,
                totalXp = 200000,
                percentage = 74
            ),
            onNavToContractDetails = { }
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoadingDefaultContractCard() {
    ValolinkTheme {
        ContractCard(
            data = null,
            onNavToContractDetails = { }
        )
    }
}
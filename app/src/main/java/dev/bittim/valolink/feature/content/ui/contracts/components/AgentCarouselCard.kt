@file:Suppress("SpellCheckingInspection")

package dev.bittim.valolink.feature.content.ui.contracts.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.feature.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.feature.content.ui.components.conditional
import dev.bittim.valolink.ui.theme.ValolinkTheme

@Composable
fun AgentCarouselCard(
    modifier: Modifier = Modifier,
    backgroundGradientColors: List<String>,
    backgroundImage: String,
    fullPortrait: String,
    roleIcon: String,
    agentName: String,
    roleName: String,
    isLocked: Boolean,
    unlockedLevels: Int,
    totalLevels: Int,
    percentage: Int,
) {
    AgentCardBase(
        modifier = modifier,
        backgroundGradientColors = backgroundGradientColors,
        backgroundImage = backgroundImage,
        isDisabled = isLocked
    ) {
        Box(
            modifier = Modifier
                .height(192.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent, Color.Black.copy(alpha = 0.3f)
                        )
                    )
                )
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .conditional(isLocked) {
                        blur(4.dp)
                    },
                model = fullPortrait,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                alignment = Alignment.Center,
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(if (isLocked) 0.3f else 1f) }),
                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_full_portrait)
            )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = agentName, style = MaterialTheme.typography.titleLarge
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .height(16.dp)
                                .aspectRatio(1f),
                            model = roleIcon,
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                            placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_role_icon)
                        )

                        Text(
                            text = roleName, style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                if (!isLocked) {
                    val animatedProgress by animateFloatAsState(
                        targetValue = (percentage / 100f),
                        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                        label = ""
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = {
                                animatedProgress
                            },
                            modifier = Modifier
                                .height(32.dp)
                                .aspectRatio(1f),
                            color = Color.White,
                            trackColor = Color.Black.copy(alpha = 0.2f),
                        )

                        Text(
                            text = "$unlockedLevels / $totalLevels",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AgentCarouselCardPreview() {
    ValolinkTheme {
        AgentCarouselCard(
            Modifier.width(300.dp),
            backgroundGradientColors = listOf(
                "f17cadff", "062261ff", "c347c7ff", "f1db6fff"
            ),
            backgroundImage = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/background.png",
            fullPortrait = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/fullportrait.png",
            roleIcon = "https://media.valorant-api.com/agents/roles/4ee40330-ecdd-4f2f-98a8-eb1243428373/displayicon.png",
            agentName = "Clove",
            roleName = "Controller",
            isLocked = false,
            3,
            10,
            30
        )
    }
}



@Preview(showBackground = true)
@Composable
fun LockedAgentCarouselCardPreview() {
    ValolinkTheme {
        AgentCarouselCard(
            Modifier.width(300.dp),
            backgroundGradientColors = listOf(
                "f17cadff", "062261ff", "c347c7ff", "f1db6fff"
            ),
            backgroundImage = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/background.png",
            fullPortrait = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/fullportrait.png",
            roleIcon = "https://media.valorant-api.com/agents/roles/4ee40330-ecdd-4f2f-98a8-eb1243428373/displayicon.png",
            agentName = "Clove",
            roleName = "Controller",
            isLocked = true,
            0,
            10,
            0
        )
    }
}
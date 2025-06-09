/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AgentDetailsSection.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.domain.model.agent.Agent
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.util.extensions.modifier.pulseAnimation

@Composable
fun AgentDetailsSection(agent: Agent?) {
    Column(
        modifier = Modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(Spacing.l)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Spacing.l),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            Crossfade(
                modifier = Modifier.animateContentSize(),
                targetState = agent?.displayName,
                label = "Agent name loading"
            ) {
                if (it == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MaterialTheme.typography.titleMedium.lineHeight.value.dp)
                            .padding(1.dp)
                            .clip(MaterialTheme.shapes.small)
                            .pulseAnimation()
                    )
                } else {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Crossfade(
                modifier = Modifier.animateContentSize(),
                targetState = agent?.description,
                label = "Agent description loading"
            ) {
                if (it == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MaterialTheme.typography.bodyMedium.lineHeight.value.dp * 5)
                            .padding(1.dp)
                            .clip(MaterialTheme.shapes.small)
                            .pulseAnimation()
                    )
                } else {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = Spacing.l),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            Crossfade(
                modifier = Modifier.animateContentSize(),
                targetState = agent?.role,
                label = "Agent role loading"
            ) {
                if (it == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MaterialTheme.typography.titleMedium.lineHeight.value.dp)
                            .padding(1.dp)
                            .clip(MaterialTheme.shapes.small)
                            .pulseAnimation()
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .height(Spacing.l)
                                .aspectRatio(1f),
                            model = it.displayIcon,
                            contentDescription = it.displayName,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_role_icon)
                        )

                        Text(
                            text = it.displayName,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            Crossfade(
                modifier = Modifier.animateContentSize(),
                targetState = agent?.role?.description,
                label = "Role description loading"
            ) {
                if (it == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MaterialTheme.typography.bodyMedium.lineHeight.value.dp * 5)
                            .padding(1.dp)
                            .clip(MaterialTheme.shapes.small)
                            .pulseAnimation()
                    )
                } else {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       AgentDetailsSection.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.components

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
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.components.pulseAnimation

@Composable
fun AgentDetailsSection(agent: Agent?) {
    Column(
        modifier = Modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
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
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
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
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .height(16.dp)
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
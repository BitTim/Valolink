/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AgentListScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:37
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.agentlist

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.content.domain.model.agent.Agent
import dev.bittim.valolink.content.ui.screens.content.contracts.components.AgentCarouselCard
import dev.bittim.valolink.content.ui.screens.content.contracts.components.AgentCarouselCardData
import dev.bittim.valolink.core.ui.util.getProgressPercent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentListScreen(
    state: AgentListState,
    initUserContract: (String) -> Unit,
    onNavBack: () -> Unit,
    onNavToAgentDetails: (String) -> Unit,
) {
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = "Agents") },
            navigationIcon = {
                IconButton(onClick = { onNavBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            scrollBehavior = scrollBehaviour,
            windowInsets = WindowInsets.statusBars
        )

        val loadingBarVisible = state.isUserDataLoading || state.isGearsLoading
        Crossfade(loadingBarVisible, label = "Loading Bar visibility") {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (it) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                    )
                }
            }
        }

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .nestedScroll(scrollBehaviour.nestedScrollConnection),
            columns = GridCells.Adaptive(AgentCarouselCard.minWidth),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                items = state.agentGears ?: List(20) { null },
                itemContent = { gear ->
                    val cardData = if (gear == null || gear.content.relation !is Agent) {
                        null
                    } else {
                        val levelCount by remember { derivedStateOf { gear.calcLevelCount() } }
                        val derivedUserContract by remember {
                            derivedStateOf {
                                state.userData?.contracts?.find { it.contract == gear.uuid }
                            }
                        }
                        val isLocked by remember {
                            derivedStateOf {
                                state.userData?.agents
                                    ?.any { it.agent == gear.content.relation.uuid }
                                    ?.not() != false
                            }
                        }

                        // This allows the smart casts below
                        val userContract = derivedUserContract
                        if (userContract == null) {
                            initUserContract(gear.uuid)
                            null
                        } else {
                            val unlockedLevels by remember {
                                derivedStateOf {
                                    userContract.levels.count()
                                }
                            }

                            val percentage by remember {
                                derivedStateOf {
                                    getProgressPercent(
                                        userContract.levels.count(),
                                        levelCount
                                    )
                                }
                            }

                            AgentCarouselCardData(
                                backgroundGradientColors = gear.content.relation.backgroundGradientColors,
                                backgroundImage = gear.content.relation.background,
                                fullPortrait = gear.content.relation.fullPortrait,
                                roleIcon = gear.content.relation.role.displayIcon,
                                agentName = gear.content.relation.displayName,
                                contractUuid = gear.uuid,
                                roleName = gear.content.relation.role.displayName,
                                totalLevels = levelCount,
                                isLocked = isLocked,
                                unlockedLevels = unlockedLevels,
                                percentage = percentage
                            )
                        }
                    }

                    AgentCarouselCard(
                        data = cardData,
                        onNavToAgentDetails = onNavToAgentDetails
                    )
                }
            )
        }
    }
}

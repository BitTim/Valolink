/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractsOverviewScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 17:49
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.overview

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.content.domain.model.agent.Agent
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.content.domain.model.contract.content.ContentType
import dev.bittim.valolink.content.ui.screens.content.contracts.components.AgentCarouselCard
import dev.bittim.valolink.content.ui.screens.content.contracts.components.AgentCarouselCardData
import dev.bittim.valolink.content.ui.screens.content.contracts.components.ContractCard
import dev.bittim.valolink.content.ui.screens.content.contracts.components.ContractCardData
import dev.bittim.valolink.core.ui.components.ConnectedButtonEntry
import dev.bittim.valolink.core.ui.components.SingleConnectedButtonGroup
import dev.bittim.valolink.core.ui.util.getProgressPercent
import java.util.UUID

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun ContractsOverviewScreen(
    state: ContractsOverviewState,
    initUserContract: (String) -> Unit,
    onArchiveTypeFilterChange: (ContentType) -> Unit,
    onNavToGearList: () -> Unit,
    onNavToAgentDetails: (String) -> Unit,
    onNavToContractDetails: (String) -> Unit,
) {
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = "Valolink") },
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null
                    )
                }
            },
            scrollBehavior = scrollBehaviour,
            windowInsets = WindowInsets.statusBars
        )

        val loadingBarVisible =
            state.isUserDataLoading || state.isActiveContractsLoading || state.isAgentGearsLoading || state.isInactiveContractsLoading

        Crossfade(
            modifier = Modifier.animateContentSize(),
            targetState = loadingBarVisible,
            label = "Loading Bar visibility"
        ) {
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp
                )
                .nestedScroll(scrollBehaviour.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            stickyHeader {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .animateItem()
                        .animateContentSize()
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp),
                        text = "Active",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            val activeItems =
                if (state.isActiveContractsLoading || state.activeContracts == null) listOf(null) else state.activeContracts
            items(
                items = activeItems,
                contentType = { Contract::class },
                key = { it?.uuid ?: UUID.randomUUID().toString() },
                itemContent = {
                    var displayIcon: String? = null
                    var backgroundImage: String? = null
                    var backgroundGradientColors: List<String> = emptyList()

                    if (it?.content?.relation is Agent) {
                        displayIcon = it.content.relation.displayIcon
                        backgroundImage = it.content.relation.background
                        backgroundGradientColors = it.content.relation.backgroundGradientColors
                    }

                    ContractCard(
                        modifier = Modifier
                            .animateItem()
                            .animateContentSize(),
                        data = if (it == null) {
                            null
                        } else {
                            val collectedXp by remember { derivedStateOf { it.getRandomCollectedXP() } }  // TODO: Placeholder values as no userdata is present yet
                            val remainingDays by remember { derivedStateOf { it.content.relation?.calcRemainingDays() } }
                            val totalXp by remember { derivedStateOf { it.calcTotalXp() } }
                            val percentage by remember {
                                derivedStateOf {
                                    getProgressPercent(
                                        collectedXp,
                                        totalXp
                                    )
                                }
                            }

                            ContractCardData(
                                displayName = it.displayName,
                                contractUuid = it.uuid,
                                displayIcon = displayIcon,
                                backgroundImage = backgroundImage,
                                backgroundGradientColors = backgroundGradientColors,
                                remainingDays = remainingDays,
                                totalXp = totalXp,
                                collectedXp = collectedXp,
                                percentage = percentage
                            )
                        },
                        onNavToContractDetails = onNavToContractDetails
                    )
                }
            )

            item {
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                        .animateItem()
                        .animateContentSize()
                )
            }

            stickyHeader {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .animateItem()
                        .animateContentSize()
                ) {
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Agents",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        IconButton(onClick = { onNavToGearList() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            item {
                val carouselState by remember(state.agentGears) {
                    derivedStateOf {
                        val itemCount = state.agentGears?.count()
                        CarouselState(itemCount = { if (itemCount == null || itemCount == 0) 5 else itemCount })
                    }
                }

                HorizontalMultiBrowseCarousel(
                    modifier = Modifier
                        .animateItem()
                        .animateContentSize(),
                    state = carouselState,
                    preferredItemWidth = AgentCarouselCard.preferredWidth,
                    itemSpacing = 8.dp,
                    minSmallItemWidth = AgentCarouselCard.minCompressedWidth,
                    maxSmallItemWidth = AgentCarouselCard.minWidth,
                    flingBehavior = CarouselDefaults.multiBrowseFlingBehavior(state = carouselState)
                ) { index ->
                    val cardData =
                        if (state.agentGears == null || state.userContracts == null || state.isAgentGearsLoading || state.isUserDataLoading) {
                            null
                        } else {
                            val gear = state.agentGears[index]
                            if (gear.content.relation !is Agent) {
                                null
                            } else {
                                val levelCount by remember { derivedStateOf { gear.calcLevelCount() } }
                                val derivedUserContract by remember {
                                    derivedStateOf {
                                        state.userContracts.find { it.contract == gear.uuid }
                                    }
                                }

                                // This allows the smart casts below
                                val userContract = derivedUserContract
                                if (userContract == null) {
                                    initUserContract(gear.uuid)
                                    null
                                } else {
                                    val unlockedLevels by remember { derivedStateOf { userContract.levels.count() } }
                                    val isLocked by remember { derivedStateOf { (state.userAgents?.any { it.agent == gear.content.relation.uuid })?.not() != false } }
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
                                        unlockedLevels = unlockedLevels,
                                        percentage = percentage,
                                        isLocked = isLocked
                                    )
                                }
                            }
                        }

                    AgentCarouselCard(
                        modifier = Modifier
                            .maskClip(shape = MaterialTheme.shapes.extraLarge)
                            .animateItem()
                            .animateContentSize(),
                        data = cardData,
                        maskedWidth = carouselItemDrawInfo.maskRect.width,
                        onNavToAgentDetails = onNavToAgentDetails
                    )
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                        .animateItem()
                        .animateContentSize()
                )
            }

            stickyHeader {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .animateItem()
                        .animateContentSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Archive",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        SingleConnectedButtonGroup(
                            modifier = Modifier.fillMaxWidth(),
                            entries = ContentType.entries.map {
                                ConnectedButtonEntry(
                                    label = it.displayName,
                                    icon = null,
                                )
                            },
                            onSelectedChanged = {
                                onArchiveTypeFilterChange(ContentType.entries[it])
                            }
                        )
                    }
                }
            }

            val inactiveItems =
                if (state.isInactiveContractsLoading || state.inactiveContracts == null) List(5) { null } else state.inactiveContracts
            items(
                items = inactiveItems,
                contentType = { Contract::class },
                key = { it?.uuid ?: UUID.randomUUID().toString() },
                itemContent = {
                    var displayIcon: String? = null
                    var backgroundImage: String? = null
                    var backgroundGradientColors: List<String> = emptyList()

                    if (it?.content?.relation is Agent) {
                        displayIcon = it.content.relation.displayIcon
                        backgroundImage = it.content.relation.background
                        backgroundGradientColors = it.content.relation.backgroundGradientColors
                    }

                    ContractCard(
                        modifier = Modifier
                            .animateItem()
                            .animateContentSize(),
                        data = if (it == null) {
                            null
                        } else {
                            val collectedXp by remember { derivedStateOf { it.getRandomCollectedXP() } }  // TODO: Placeholder values as no userdata is present yet
                            val remainingDays by remember { derivedStateOf { it.content.relation?.calcRemainingDays() } }
                            val totalXp by remember { derivedStateOf { it.calcTotalXp() } }
                            val percentage by remember {
                                derivedStateOf {
                                    getProgressPercent(
                                        collectedXp,
                                        totalXp
                                    )
                                }
                            }

                            ContractCardData(
                                displayName = it.displayName,
                                contractUuid = it.uuid,
                                displayIcon = displayIcon,
                                backgroundImage = backgroundImage,
                                backgroundGradientColors = backgroundGradientColors,
                                remainingDays = remainingDays,
                                totalXp = totalXp,
                                collectedXp = collectedXp,
                                percentage = percentage
                            )
                        },
                        onNavToContractDetails = onNavToContractDetails
                    )
                }
            )

            item {
                Spacer(
                    modifier = Modifier
                        .height(4.dp)
                        .animateItem()
                        .animateContentSize()
                )
            }
        }
    }
}

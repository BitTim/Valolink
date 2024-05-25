package dev.bittim.valolink.feature.content.ui.screens.contracts.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.feature.content.domain.model.game.agent.Agent
import dev.bittim.valolink.feature.content.domain.model.game.contract.Contract
import dev.bittim.valolink.feature.content.ui.screens.contracts.components.AgentCarouselCard
import dev.bittim.valolink.feature.content.ui.screens.contracts.components.DefaultContractCard
import java.util.Random
import kotlin.math.floor

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun ContractsMainScreen(
    state: ContractsMainState,
    onArchiveTypeFilterChange: (ArchiveTypeFilter) -> Unit,
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

        if (state.isLoading) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
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
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp),
                        text = "Active",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            items(items = state.activeContracts,
                  contentType = { Contract::class },
                  key = { it.uuid },
                  itemContent = {
                      var displayIcon: String? = null
                      var backgroundImage: String? = null
                      var backgroundGradientColors: List<String> = listOf()

                      if (it.content.relation is Agent) {
                          displayIcon = it.content.relation.displayIcon
                          backgroundImage = it.content.relation.background
                          backgroundGradientColors = it.content.relation.backgroundGradientColors
                      }

                      val collected = it.getRandomCollectedXP()

                      DefaultContractCard(
                          displayName = it.displayName,
                          contractUuid = it.uuid,
                          displayIcon = displayIcon,
                          backgroundImage = backgroundImage,
                          backgroundGradientColors = backgroundGradientColors,
                          remainingDays = it.content.relation?.calcRemainingDays(),
                          totalXp = it.calcTotalXp(),
                          // TODO: Placeholder values as no userdata is present yet
                          collectedXp = collected,
                          percentage = floor(
                              (collected.toFloat() / it.calcTotalXp()
                                  .toFloat()) * 100f
                          ).toInt(),
                          onNavToContractDetails = onNavToContractDetails
                      )
                  })

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            stickyHeader {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
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
                HorizontalMultiBrowseCarousel(
                    state = state.agentGearCarouselState,
                    preferredItemWidth = AgentCarouselCard.preferredWidth,
                    itemSpacing = 8.dp,
                    minSmallItemWidth = AgentCarouselCard.minCompressedWidth,
                    maxSmallItemWidth = AgentCarouselCard.minCompressedWidth,
                    flingBehavior = CarouselDefaults.multiBrowseFlingBehavior(state = state.agentGearCarouselState)
                ) { index ->
                    val gear = state.agentGears[index]

                    // TODO: Random values for testing
                    val random = Random()
                    var isLockedList by remember { mutableStateOf(listOf<Boolean>()) }
                    var unlockedLevelsList by remember { mutableStateOf(listOf<Int>()) }
                    if (isLockedList.isEmpty()) {
                        isLockedList = List(state.agentGears.count()) { _ -> random.nextBoolean() }
                    }
                    if (unlockedLevelsList.isEmpty()) {
                        unlockedLevelsList =
                            List(state.agentGears.count()) { i -> random.nextInt(state.agentGears[i].calcLevelCount()) }
                    }

                    if (gear.content.relation is Agent) {
                        AgentCarouselCard(
                            backgroundGradientColors = gear.content.relation.backgroundGradientColors,
                            backgroundImage = gear.content.relation.background,
                            fullPortrait = gear.content.relation.fullPortrait,
                            roleIcon = gear.content.relation.role.displayIcon,
                            agentName = gear.content.relation.displayName,
                            contractUuid = gear.uuid,
                            roleName = gear.content.relation.role.displayName,
                            totalLevels = gear.calcLevelCount(),
                            // TODO: Placeholder values as no userdata is present yet
                            unlockedLevels = unlockedLevelsList[index],
                            percentage = floor(
                                (unlockedLevelsList[index].toFloat() / gear.calcLevelCount()
                                    .toFloat()) * 100f
                            ).toInt(),
                            isLocked = isLockedList[index],
                            maskedWidth = carouselItemInfo.maskRect.width,
                            onNavToAgentDetails = onNavToAgentDetails
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            stickyHeader {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
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

                        SingleChoiceSegmentedButtonRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ArchiveTypeFilter.entries.forEachIndexed { index, entry ->
                                SegmentedButton(
                                    selected = entry == state.archiveTypeFilter,
                                    onClick = { onArchiveTypeFilterChange(entry) },
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = index,
                                        count = ArchiveTypeFilter.entries.count()
                                    )
                                ) {
                                    Text(text = entry.displayName)
                                }
                            }
                        }
                    }
                }
            }

            items(items = state.inactiveContracts,
                  contentType = { Contract::class },
                  key = { it.uuid },
                  itemContent = {
                      var displayIcon: String? = null
                      var backgroundImage: String? = null
                      var backgroundGradientColors: List<String> = listOf()

                      if (it.content.relation is Agent) {
                          displayIcon = it.content.relation.displayIcon
                          backgroundImage = it.content.relation.background
                          backgroundGradientColors = it.content.relation.backgroundGradientColors
                      }

                      val collected = it.getRandomCollectedXP()

                      DefaultContractCard(
                          displayName = it.displayName,
                          contractUuid = it.uuid,
                          displayIcon = displayIcon,
                          backgroundImage = backgroundImage,
                          backgroundGradientColors = backgroundGradientColors,
                          remainingDays = it.content.relation?.calcRemainingDays(),
                          totalXp = it.calcTotalXp(),
                          // TODO: Placeholder values as no userdata is present yet
                          collectedXp = collected,
                          percentage = floor(
                              (collected.toFloat() / it.calcTotalXp()
                                  .toFloat()) * 100f
                          ).toInt(),
                          onNavToContractDetails = onNavToContractDetails
                      )
                  })

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
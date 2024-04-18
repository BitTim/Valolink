package dev.bittim.valolink.feature.content.ui.contracts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.feature.content.domain.model.agent.Agent
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import dev.bittim.valolink.feature.content.ui.contracts.components.DefaultContractCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContractsScreen(
    state: ContractsState, onArchiveTypeFilterChange: (ArchiveTypeFilter) -> Unit
) {
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
            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        stickyHeader {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Active",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
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
                
                DefaultContractCard(
                    displayName = it.displayName,
                    displayIcon = displayIcon,
                    backgroundImage = backgroundImage,
                    backgroundGradientColors = backgroundGradientColors,
                    remainingDays = it.calcRemainingDays(),
                    totalXp = it.calcTotalXp(),
                    // TODO: Placeholder values as no userdata is present yet
                    collectedXp = 148660,
                    percentage = 74
                )
            })

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        stickyHeader {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Agents",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        //TODO: Replace with Agent Carousel
        items(
            items = state.agentGears,
            contentType = { Contract::class },
            key = { it.uuid },
            itemContent = {
                if (it.content.relation is Agent) {
                    DefaultContractCard(
                        displayName = it.content.relation.displayName,
                        displayIcon = it.content.relation.displayIcon,
                        backgroundImage = it.content.relation.background,
                        backgroundGradientColors = it.content.relation.backgroundGradientColors,
                        remainingDays = it.calcRemainingDays(),
                        totalXp = it.calcTotalXp(),
                        // TODO: Placeholder values as no userdata is present yet
                        collectedXp = 14000,
                        percentage = 15
                    )
                }
            })

        item {
            Spacer(modifier = Modifier.height(20.dp))
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
                        .padding(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Archive", style = MaterialTheme.typography.headlineMedium
                    )

                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ArchiveTypeFilter.entries.forEachIndexed { index, entry ->
                            SegmentedButton(
                                selected = entry == state.archiveTypeFilter,
                                onClick = { onArchiveTypeFilterChange(entry) },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index, count = ArchiveTypeFilter.entries.count()
                                )
                            ) {
                                Text(text = entry.displayName)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
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
                
                DefaultContractCard(
                    displayName = it.displayName,
                    displayIcon = displayIcon,
                    backgroundImage = backgroundImage,
                    backgroundGradientColors = backgroundGradientColors,
                    remainingDays = it.calcRemainingDays(),
                    totalXp = it.calcTotalXp(),
                    // TODO: Placeholder values as no userdata is present yet
                    collectedXp = 230000,
                    percentage = 39
                )
            })

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
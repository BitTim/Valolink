package dev.bittim.valolink.feature.content.ui.contracts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.feature.content.domain.model.agent.Agent
import dev.bittim.valolink.feature.content.domain.model.agent.Role
import dev.bittim.valolink.feature.content.domain.model.contract.Content
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import dev.bittim.valolink.feature.content.ui.contracts.components.DefaultContractCard
import dev.bittim.valolink.ui.theme.ValolinkTheme
import java.util.UUID

@Composable
fun ContractsScreen(
    state: ContractsState, getContracts: () -> Unit
) {
    if (state.activeContracts.isEmpty() && state.agentContracts.isEmpty() && state.inactiveContracts.isEmpty()) {
        LaunchedEffect(key1 = Unit) {
            getContracts()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Active", style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(items = state.activeContracts, itemContent = {
            DefaultContractCard(
                displayName = it.displayName,
                displayIcon = it.relation?.displayIcon,
                backgroundImage = it.relation?.background,
                backgroundGradientColors = it.relation?.backgroundGradientColors ?: listOf(),
                // TODO: Placeholder values as no userdata is present yet
                remainingDays = 5,
                collectedXp = 148660,
                totalXp = 200000,
                percentage = 74
            )
        })

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Agents", style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        //TODO: Replace with Agent Carousel
        items(items = state.agentContracts, itemContent = {
            DefaultContractCard(
                displayName = it.relation?.displayName ?: "",
                displayIcon = it.relation?.displayIcon,
                backgroundImage = it.relation?.background,
                backgroundGradientColors = it.relation?.backgroundGradientColors ?: listOf(),
                // TODO: Placeholder values as no userdata is present yet
                remainingDays = 16,
                collectedXp = 14000,
                totalXp = 940000,
                percentage = 15
            )
        })

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Archive", style = MaterialTheme.typography.headlineMedium
            )
        }

        //TODO: Implement into State and add filtering in ViewModel
        item {
            val options = listOf("Season", "Event", "Agent")
            var selectedIndex by remember { mutableIntStateOf(0) }
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(selected = index == selectedIndex,
                        onClick = { selectedIndex = index },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        )
                    ) {
                        Text(text = label)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(items = state.inactiveContracts, itemContent = {
            DefaultContractCard(
                displayName = it.displayName,
                displayIcon = it.relation?.displayIcon,
                backgroundImage = it.relation?.background,
                backgroundGradientColors = it.relation?.backgroundGradientColors ?: listOf(),
                remainingDays = null,
                // TODO: Placeholder values as no userdata is present yet
                collectedXp = 230000,
                totalXp = 586723,
                percentage = 39
            )
        })

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Suppress("SpellCheckingInspection")
@Composable
fun ContractsScreenPreview() {
    ValolinkTheme {
        ContractsScreen(
            state = ContractsState(
                agentContracts = listOf(
                    Contract(
                        UUID.randomUUID().toString(),
                        "",
                        null,
                        shipIt = false,
                        useLevelVPCostOverride = false,
                        levelVPCostOverride = 0,
                        freeRewardScheduleUuid = UUID.randomUUID().toString(),
                        content = Content(
                            null, null, null, 0, listOf()
                        ),
                        relation = Agent(
                            uuid = "",
                            displayName = "Lol",
                            description = "",
                            developerName = "",
                            characterTags = listOf(),
                            displayIcon = "",
                            displayIconSmall = "",
                            bustPortrait = "",
                            fullPortrait = "",
                            fullPortraitV2 = "",
                            killfeedPortrait = "",
                            background = "",
                            backgroundGradientColors = listOf(
                                "f17cadff", "062261ff", "c347c7ff", "f1db6fff"
                            ),
                            isFullPortraitRightFacing = false,
                            isAvailableForTest = false,
                            isBaseContent = false,
                            role = Role(
                                uuid = UUID.randomUUID().toString(),
                                displayName = "Role",
                                description = "",
                                displayIcon = ""
                            ),
                            recruitment = null,
                            abilities = listOf(),
                        )
                    )
                )
            )
        ) {}
    }
}
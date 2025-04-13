/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractDetailsScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:30
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.contractdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.components.AgentRewardCard
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.components.AgentRewardCardData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractDetailsScreen(
    state: ContractDetailsState,
    uuid: String,
    fetchDetails: (uuid: String) -> Unit,
    onNavBack: () -> Unit,
    onNavLevelList: (uuid: String) -> Unit,
    onNavToAgentDetails: (uuid: String) -> Unit,
    onNavToLevelDetails: (level: String, contract: String) -> Unit,
) {
    // Fetch details if they haven't been fetched yet
    if (state.contract == null || state.vp == null) {
        if (!state.isContractLoading && !state.isCurrencyLoading) {
            fetchDetails(uuid)
        }
    }

    val isLoading = state.isContractLoading || state.isCurrencyLoading
    if (isLoading) CircularProgressIndicator() // TODO: Temporary

    if (state.contract != null) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val scrollState = rememberScrollState()

        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                TopAppBar(
                    title = {
                        Text(text = state.contract.displayName)
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rewards",
                            style = MaterialTheme.typography.titleLarge
                        )

                        IconButton(onClick = { onNavLevelList(uuid) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                                contentDescription = null
                            )
                        }
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.contract.content.chapters.flatMap { it.levels },
                            itemContent = { level ->
                                val reward = level.rewards.find { !it.isFreeReward }?.relation

                                if (reward != null) {
                                    AgentRewardCard(
                                        data = AgentRewardCardData(
                                            name = reward.displayName,
                                            levelUuid = level.uuid, // TODO: Replace with uuid to agent gear for recruitments
                                            type = reward.type,
                                            levelName = level.name,
                                            contractName = state.contract.displayName,
                                            rewardCount = level.rewards.count(),
                                            previewIcon = reward.previewImages.first().first
                                                ?: "",
                                            background = reward.background,
                                            price = level.vpCost,
                                            amount = reward.amount,
                                            currencyIcon = state.vp?.displayIcon ?: ""
                                        ),
                                        onNavToLevelDetails = { levelUuid ->
                                            if (reward.type == RewardType.AGENT) {
                                                onNavToAgentDetails(reward.uuid)
                                            } else {
                                                onNavToLevelDetails(
                                                    levelUuid,
                                                    state.contract.uuid
                                                )
                                            }
                                        },
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

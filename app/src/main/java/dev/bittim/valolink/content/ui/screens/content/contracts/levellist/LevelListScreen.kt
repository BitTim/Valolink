/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LevelListScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 03:44
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.levellist

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.content.domain.model.agent.Agent
import dev.bittim.valolink.content.domain.model.contract.chapter.Level
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.dialogs.ContractResetAlertDialog
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.dialogs.LevelResetAlertDialog
import dev.bittim.valolink.core.ui.components.rewardCard.RewardListCard
import dev.bittim.valolink.core.ui.components.rewardCard.RewardListCardData
import java.time.Instant
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelListScreen(
    state: LevelListState,
    uuid: String,
    fetchDetails: (uuid: String) -> Unit,
    initUserContract: () -> Unit,
    resetContract: () -> Unit,
    resetLevel: (uuid: String) -> Unit,
    onNavBack: () -> Unit,
    onNavToAgentDetails: (String) -> Unit,
    onNavToLevelDetails: (level: String, contract: String) -> Unit,
) {
    // Fetch details if they haven't been fetched yet
    if (state.contract == null || state.currency == null) {
        if (!state.isContractLoading && !state.isCurrencyLoading) {
            fetchDetails(uuid)
        }
    }

    val numRewardsVisible = 10
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()

    val contractLevels = state.contract?.content?.chapters?.flatMap { it.levels }
    val contractRelation = state.contract?.content?.relation
    val isLocked = when (contractRelation is Agent) {
        true -> state.userData?.agents?.any { it.agent == contractRelation.uuid } != true
        false -> state.contract?.content?.relation?.endTime?.isBefore(Instant.now()) != false
    }

    val userContract = state.userData?.contracts?.find {
        it.contract == state.contract?.uuid
    }

    if (userContract == null) {
        if (!state.isUserDataLoading) {
            initUserContract()
        }
    }

    // Scroll to the currently active reward
    LaunchedEffect(
        userContract?.levels?.count(),
        state.contract
    ) {
        val targetIndex = contractLevels?.indexOfFirst {
            it.uuid == userContract?.levels?.lastOrNull()?.uuid
        } ?: return@LaunchedEffect

        if (targetIndex > -1) state.rewardListState.animateScrollToItem(targetIndex)
    }

    var isMenuExpanded: Boolean by remember { mutableStateOf(false) }
    var isContractResetAlertShown: Boolean by remember { mutableStateOf(false) }
    var isLevelResetAlertShown: Boolean by remember { mutableStateOf(false) }

    var targetLevelUuid: String by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = state.contract?.displayName ?: "") },
            navigationIcon = {
                IconButton(onClick = { onNavBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = { isMenuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )

                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            enabled = !isLocked,
                            text = { Text(text = "Reset") },
                            onClick = {
                                isContractResetAlertShown = true
                                isMenuExpanded = false
                            }
                        )
                    }
                }
            },
            scrollBehavior = scrollBehaviour,
            windowInsets = WindowInsets.statusBars
        )

        val loadingBarVisible =
            state.isUserDataLoading || state.isContractLoading || state.isCurrencyLoading

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
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = state.rewardListState
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            val items = state.contract?.content?.chapters?.flatMap { it.levels }

            items(
                items = items ?: List(numRewardsVisible) { null },
                key = { it?.uuid ?: UUID.randomUUID().toString() },
                itemContent = { level ->
                    val reward = level?.rewards?.find { !it.isFreeReward }?.relation
                    val rewardCardData = if (level == null || reward == null) {
                        null
                    } else {
                        RewardListCardData(
                            name = reward.displayName,
                            levelUuid = level.uuid,
                            type = reward.type,
                            levelName = level.name,
                            contractName = state.contract?.displayName ?: "",
                            rewardCount = level.rewards.count(),
                            amount = reward.amount,
                            useXP = true,
                            xpTotal = level.xp,
                            displayIcon = reward.displayIcon,
                            background = reward.background,
                        )
                    }

                    RewardListCard(
                        data = rewardCardData,
                        xpCollected = 0, // TODO: Replace with actual user data
                        isLocked = isLocked,
                        isOwned = userContract?.levels?.any { it.level == level?.uuid } == true,
                        onNavToLevelDetails = { levelUuid ->
                            if (reward?.type == RewardType.AGENT) {
                                onNavToAgentDetails(reward.uuid)
                            } else {
                                onNavToLevelDetails(
                                    levelUuid,
                                    state.contract?.uuid ?: ""
                                )
                            }
                        }
                    )
                }
            )

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // --------------------------------
    //  Alerts
    // --------------------------------

    if (isContractResetAlertShown) {
        ContractResetAlertDialog(
            contractName = state.contract?.displayName ?: "",
            onDismiss = { isContractResetAlertShown = false },
            onConfirm = resetContract
        )
    }

    if (isLevelResetAlertShown && userContract?.levels?.any { it.level == targetLevelUuid } == true) {
        val stagedLevels = Level.reverseTraverse(
            contractLevels ?: emptyList(),
            userContract.levels.lastOrNull()?.level,
            targetLevelUuid,
            true
        )

        LevelResetAlertDialog(
            levels = stagedLevels,
            currency = state.currency,
            onDismiss = { isLevelResetAlertShown = false },
            onConfirm = {
                stagedLevels.forEach {
                    resetLevel(it.uuid)
                }
            }
        )
    }
}

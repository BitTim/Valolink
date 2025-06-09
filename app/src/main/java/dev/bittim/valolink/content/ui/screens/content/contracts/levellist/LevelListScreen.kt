/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LevelListScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
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
import dev.bittim.valolink.content.domain.model.agent.Agent
import dev.bittim.valolink.content.domain.model.contract.chapter.Level
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.dialogs.ContractResetAlertDialog
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.dialogs.LevelResetAlertDialog
import dev.bittim.valolink.core.ui.components.rewardCard.RewardListCard
import dev.bittim.valolink.core.ui.components.rewardCard.RewardListCardData
import dev.bittim.valolink.core.ui.theme.Spacing
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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val contractLevels = state.contract?.content?.chapters?.flatMap { it.levels }
    val contractRelation = state.contract?.content?.relation
    val isLocked = when (contractRelation is Agent) {
        true -> state.userAgents?.any { it.agent == contractRelation.uuid } != true
        false -> state.contract?.content?.relation?.endTime?.isBefore(Instant.now()) != false
    }

    if (state.userContract == null) {
        if (!state.isUserAgentsLoading) {
            initUserContract()
        }
    }

    // Scroll to the currently active reward
    LaunchedEffect(
        state.userContract?.levels?.count(),
        state.contract
    ) {
        val targetIndex = contractLevels?.indexOfFirst {
            it.uuid == state.userContract?.levels?.lastOrNull()?.uuid
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
            scrollBehavior = scrollBehavior,
            windowInsets = WindowInsets.statusBars
        )

        val loadingBarVisible =
            state.isUserAgentsLoading || state.isContractLoading || state.isCurrencyLoading

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
                            .height(Spacing.xs)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .height(Spacing.xs)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.padding(horizontal = Spacing.l),
            verticalArrangement = Arrangement.spacedBy(Spacing.m),
            state = state.rewardListState
        ) {
            item {
                Spacer(modifier = Modifier.height(Spacing.l))
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
                        isOwned = state.userContract?.levels?.any { it.level == level?.uuid } == true,
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
                Spacer(modifier = Modifier.height(Spacing.l))
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

    if (isLevelResetAlertShown && state.userContract?.levels?.any { it.level == targetLevelUuid } == true) {
        val stagedLevels = Level.reverseTraverse(
            contractLevels ?: emptyList(),
            state.userContract.levels.lastOrNull()?.level,
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

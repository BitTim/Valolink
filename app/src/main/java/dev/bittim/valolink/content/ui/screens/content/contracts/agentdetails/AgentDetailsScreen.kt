/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AgentDetailsScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 20:11
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.domain.model.agent.Agent
import dev.bittim.valolink.content.domain.model.contract.chapter.Level
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.components.AbilitySection
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.components.AgentDetailsSection
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.dialogs.ContractResetAlertDialog
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.dialogs.LevelResetAlertDialog
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.dialogs.LevelUnlockAlertDialog
import dev.bittim.valolink.core.ui.components.DetailScreen
import dev.bittim.valolink.core.ui.components.ShaderGradientBackdrop
import dev.bittim.valolink.core.ui.components.rewardCard.RewardCard
import dev.bittim.valolink.core.ui.components.rewardCard.RewardCardData
import dev.bittim.valolink.core.ui.util.color.ShaderGradient
import dev.bittim.valolink.core.ui.util.extensions.modifier.conditional
import dev.bittim.valolink.core.ui.util.getProgressPercent
import java.util.UUID
import kotlin.math.ceil

@Composable
fun AgentDetailsScreen(
    state: AgentDetailsState,
    uuid: String,
    fetchDetails: (uuid: String) -> Unit,
    unlockAgent: () -> Unit,
    resetAgent: () -> Unit,
    initUserContract: () -> Unit,
    unlockLevel: (uuid: String?) -> Unit,
    resetLevel: (uuid: String?) -> Unit,
    onNavBack: () -> Unit,
    onNavLevelList: (uuid: String) -> Unit,
    onNavLevelDetails: (level: String, contract: String) -> Unit,
) {
    // Fetch details if they haven't been fetched yet
    if (state.agentGear == null) {
        if (!state.isContractLoading) {
            fetchDetails(uuid)
        }
    }

    // --------------------------------
    //  Logic
    // --------------------------------

    val numRewardsVisible =
        ceil(LocalConfiguration.current.screenWidthDp / RewardCard.width.value).toInt()

    val agent = state.agentGear?.content?.relation as? Agent?
    val isLocked = state.userData?.agents?.any { it.agent == agent?.uuid } != true

    val agentGearLevels = state.agentGear?.content?.chapters?.flatMap { it.levels }
    val userContract = state.userData?.contracts?.find {
        it.contract == state.agentGear?.uuid
    }

    if (userContract == null) {
        if (!state.isUserDataLoading) {
            initUserContract()
        }
    }

    // Scroll to the currently active reward
    LaunchedEffect(
        userContract?.levels?.count(),
        state.agentGear
    ) {
        val targetIndex = agentGearLevels?.indexOfFirst {
            it.uuid == userContract?.levels?.lastOrNull()?.uuid
        } ?: return@LaunchedEffect

        if (targetIndex > -1) state.rewardListState.animateScrollToItem(targetIndex)
    }

    var isMenuExpanded: Boolean by remember { mutableStateOf(false) }
    var isAgentResetAlertShown: Boolean by remember { mutableStateOf(false) }
    var isRewardUnlockAlertShown: Boolean by remember { mutableStateOf(false) }
    var isRewardResetAlertShown: Boolean by remember { mutableStateOf(false) }

    var targetLevelUuid: String by remember { mutableStateOf("") }

    // --------------------------------
    //  Layout
    // --------------------------------

    DetailScreen(
        isLoading = state.isContractLoading || state.isUserDataLoading || state.isCurrencyLoading,

        cardBackground = {
            if (agent != null) {
                ShaderGradientBackdrop(
                    modifier = Modifier.fillMaxSize(),
                    gradient = ShaderGradient.fromList(agent.backgroundGradientColors),
                    backgroundImage = agent.background,
                    isDisabled = isLocked
                ) {}
            }
        },
        cardImage = {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = WindowInsets.displayCutout
                            .asPaddingValues()
                            .calculateTopPadding()
                    )
                    .conditional(isLocked) {
                        blur(4.dp)
                    },
                model = agent?.fullPortrait,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                    setToSaturation(
                        if (isLocked) 0.3f else 1f
                    )
                }),
                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_full_portrait)
            )
        },
        cardContent = {
            if (!isLocked) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.Bottom
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = agent?.displayName ?: "",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .height(16.dp)
                                    .aspectRatio(1f),
                                model = agent?.role?.displayIcon,
                                contentDescription = null,
                                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_role_icon)
                            )
                            Text(
                                text = agent?.role?.displayName ?: "",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    val totalLevels = state.agentGear?.calcLevelCount() ?: 0
                    val unlockedLevels = userContract?.levels?.count() ?: 0
                    val percentage = getProgressPercent(unlockedLevels, totalLevels)

                    val animatedProgress: Float by animateFloatAsState(
                        targetValue = (percentage / 100f),
                        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                        label = ""
                    )

                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        trackColor = Color.Black.copy(alpha = 0.3f),
                        progress = { animatedProgress })

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "$unlockedLevels / $totalLevels")
                        Text(text = "$percentage %")
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = agent?.displayName ?: "",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .height(16.dp)
                                    .aspectRatio(1f),
                                model = agent?.role?.displayIcon,
                                contentDescription = null,
                                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_role_icon)
                            )
                            Text(
                                text = agent?.role?.displayName ?: "",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    FilledTonalButton(onClick = unlockAgent) {
                        AsyncImage(
                            modifier = Modifier
                                .width(16.dp)
                                .aspectRatio(1f),
                            model = state.dough?.displayIcon,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint),
                            placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_kingdom_kreds)
                        )

                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = "8000"  // TODO: Move to a different data source
                        )
                    }
                }
            }
        },
        content = {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Details",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            AgentDetailsSection(agent)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Abilities",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            AbilitySection(abilities = agent?.abilities)

            Spacer(modifier = Modifier.height(24.dp))

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

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                state = state.rewardListState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val items =
                    state.agentGear?.content?.chapters?.flatMap { chapter -> chapter.levels }

                items(
                    items = items ?: List(numRewardsVisible) { null },
                    key = { it?.uuid ?: UUID.randomUUID().toString() },
                    itemContent = { level ->
                        val reward = level?.rewards?.find { !it.isFreeReward }?.relation
                        val rewardCardData = if (level == null || reward == null) {
                            null
                        } else {
                            RewardCardData(
                                name = reward.displayName,
                                levelUuid = level.uuid,
                                type = reward.type,
                                levelName = level.name,
                                contractName = state.agentGear?.displayName ?: "",
                                rewardCount = level.rewards.count(),
                                previewIcon = reward.previewImages.first().first ?: "",
                                background = reward.background,
                                useXP = false,
                                price = level.doughCost,
                                amount = reward.amount,
                                currencyIcon = state.dough?.displayIcon ?: "",
                            )
                        }

                        RewardCard(
                            data = rewardCardData,
                            isLocked = isLocked,
                            isOwned = userContract?.levels?.any { it.level == level?.uuid } == true,
                            unlockReward = {
                                if (userContract?.levels?.lastOrNull()?.level == level?.dependency) {
                                    // Unlock just one
                                    unlockLevel(level?.uuid)
                                } else {
                                    if (level == null) return@RewardCard

                                    // Unlock multiple at the same time
                                    targetLevelUuid = level.uuid
                                    isRewardUnlockAlertShown = true
                                }
                            },
                            onNavToLevelDetails = { levelUuid ->
                                onNavLevelDetails(
                                    levelUuid,
                                    state.agentGear?.uuid ?: ""
                                )
                            }
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Contract: ${state.agentGear?.uuid}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Text(
                    text = "Agent: ${agent?.uuid}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Text(
                    text = "Provided UUID: $uuid",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        },
        dropdown = {
            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    enabled = !isLocked,
                    text = { Text(text = "Reset") },
                    onClick = {
                        isAgentResetAlertShown = true
                        isMenuExpanded = false
                    }
                )
            }
        },

        onOpenDropdown = { isMenuExpanded = true },
        onNavBack = onNavBack,
    )

    // --------------------------------
    //  Alerts
    // --------------------------------

    if (isAgentResetAlertShown) {
        ContractResetAlertDialog(
            contractName = agent?.displayName ?: "",
            onDismiss = { isAgentResetAlertShown = false },
            onConfirm = resetAgent
        )
    }

    if (isRewardUnlockAlertShown && userContract?.levels?.any { it.level == targetLevelUuid } == false) {
        val stagedLevels = Level.reverseTraverse(
            agentGearLevels ?: emptyList(),
            targetLevelUuid,
            userContract.levels.lastOrNull()?.level,
            false
        )

        LevelUnlockAlertDialog(
            levels = stagedLevels,
            state.dough,
            onDismiss = { isRewardUnlockAlertShown = false },
            onConfirm = {
                stagedLevels.forEach {
                    unlockLevel(it.uuid)
                }
            }
        )
    }

    if (isRewardResetAlertShown && userContract?.levels?.any { it.level == targetLevelUuid } == true) {
        val stagedLevels = Level.reverseTraverse(
            agentGearLevels ?: emptyList(),
            userContract.levels.lastOrNull()?.level,
            targetLevelUuid,
            true
        )

        LevelResetAlertDialog(
            levels = stagedLevels,
            state.dough,
            onDismiss = { isRewardResetAlertShown = false },
            onConfirm = {
                stagedLevels.forEach {
                    resetLevel(it.uuid)
                }
            }
        )
    }
}

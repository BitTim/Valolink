/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LevelDetailsScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 13:41
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.bittim.valolink.R
import dev.bittim.valolink.content.domain.model.contract.chapter.Level
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.dialogs.LevelResetAlertDialog
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.dialogs.LevelUnlockAlertDialog
import dev.bittim.valolink.content.ui.screens.content.contracts.components.LevelBackdrop
import dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components.HeaderSection
import dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components.LevelHeaderData
import dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components.RelationsSection
import dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components.RelationsSectionContentData
import dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components.RelationsSectionRelation
import dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components.RelationsSectionState
import dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components.RelationsSectionUserData
import dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components.UnlockSection
import dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components.UnlockSectionData
import dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components.VariantPreviewCluster
import dev.bittim.valolink.core.ui.components.DetailScreen
import dev.bittim.valolink.core.ui.components.rewardCard.RewardListCardData

@Composable
fun LevelDetailsScreen(
    state: LevelDetailsState,
    uuid: String,
    contract: String,
    fetchDetails: (uuid: String, contract: String) -> Unit,
    initUserContract: () -> Unit,
    unlockLevel: (uuid: String?, isPurchased: Boolean) -> Unit,
    resetLevel: (uuid: String) -> Unit,
    onNavBack: () -> Unit,
    onNavToLevelDetails: (level: String, contract: String) -> Unit,
) {
    // Fetch details if they haven't been fetched yet
    if (state.level == null || state.contract == null || state.level.uuid != uuid || state.contract.uuid != contract) {
        if (!state.isLevelLoading && !state.isContractLoading) {
            fetchDetails(uuid, contract)
        }
    }

    // --------------------------------
    //  Logic
    // --------------------------------

    val rewards = state.level?.rewards
    val contractLevels = state.contract?.content?.chapters?.flatMap { it.levels }
    if (state.userContract == null) {
        if (!state.isUserDataLoading) {
            initUserContract()
        }
    }

    val isLocked = state.userContract?.levels?.any { it.level == state.level?.uuid }?.not()

    var isMenuExpanded: Boolean by remember { mutableStateOf(false) }
    var isRewardUnlockAlertShown: Boolean by remember { mutableStateOf(false) }
    var isRewardResetAlertShown: Boolean by remember { mutableStateOf(false) }

    var selectedVariant by remember { mutableIntStateOf(0) }
    var selectedReward by remember { mutableIntStateOf(0) }

    val reward by remember(rewards, selectedReward) {
        derivedStateOf {
            rewards?.get(selectedReward)
        }
    }

    // --------------------------------
    //  Layout
    // --------------------------------

    DetailScreen(
        isLoading = state.isLevelLoading,

        cardBackground = {
            LevelBackdrop(
                isDisabled = false,
                backgroundImage = reward?.relation?.background
            ) {}
        },
        cardImage = {
            Box {
                Crossfade(
                    targetState = selectedVariant,
                    label = "Preview Image Transition"
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = WindowInsets.displayCutout
                                    .asPaddingValues()
                                    .calculateTopPadding(),
                                end = 16.dp,
                                bottom = 16.dp,
                                start = 16.dp,
                            ),
                        model = ImageRequest
                            .Builder(
                                LocalContext.current
                            )
                            .data(reward?.relation?.previewImages?.getOrNull(it)?.first ?: "")
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_full_portrait)
                    )
                }

            }
        },
        cardContent = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {
                Crossfade(
                    reward?.relation?.previewImages?.map { it.second } ?: emptyList(),
                    label = "VariantCluster appearance"
                ) { checkedVariants ->
                    if (checkedVariants.count() > 1) {
                        VariantPreviewCluster(
                            modifier = Modifier.animateContentSize(),
                            variants = checkedVariants,
                            selected = selectedVariant,
                            onSelected = { selectedVariant = it }
                        )
                    }
                }
            }
        },
        content = {
            Column {
                val checkedReward = reward
                val levelHeaderData = if (
                    checkedReward?.relation == null ||
                    state.contract == null
                ) {
                    null
                } else {
                    LevelHeaderData(
                        displayName = if (checkedReward.amount > 1) "${checkedReward.amount} ${checkedReward.relation.displayName}" else checkedReward.relation.displayName,
                        type = checkedReward.relation.type,
                        displayIcon = checkedReward.relation.displayIcon,
                        levelName = state.level?.name ?: "",
                        contractName = state.contract.displayName,
                    )
                }

                HeaderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    levelHeaderData = levelHeaderData,
                    rewards = rewards,
                    onRewardSelected = {
                        selectedVariant = 0
                        selectedReward = it
                    }
                )

                val unlockSectionData = if (isLocked == null) null else UnlockSectionData(
                    currencyIcon = state.unlockCurrency?.displayIcon,
                    price = state.price,
                    xpTotal = if (state.isGear) -1 else state.level?.xp ?: 0,
                    xpProgress = 25, // TODO: Replace with actual user values,
                    isLocked = false, // TODO: Replace with proper isLocked state
                    isOwned = !isLocked
                )

                UnlockSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    data = unlockSectionData,
                    onUnlock = {
                        if (state.userContract?.levels?.lastOrNull()?.level == state.level?.dependency) {
                            // Unlock just one
                            unlockLevel(state.level?.uuid, true)
                        } else {
                            // Unlock multiple at the same time
                            isRewardUnlockAlertShown = true
                        }
                    },
                )

                val prevLevel = state.previousLevel
                val prevLevelReward = prevLevel?.rewards?.find { !it.isFreeReward }?.relation
                val isPrevLevelOwned =
                    state.userContract?.levels?.any { it.level == prevLevel?.uuid }

                val nextLevel = state.nextLevel
                val nextLevelReward = nextLevel?.rewards?.find { !it.isFreeReward }?.relation
                val isNextLevelOwned =
                    state.userContract?.levels?.any { it.level == nextLevel?.uuid }

                val relationsSectionContentData =
                    if (state.isLevelRelationsLoading || state.contract == null) null else RelationsSectionContentData(
                        relations = listOfNotNull(
                            if (prevLevel == null || prevLevelReward == null || isPrevLevelOwned == null) null else RelationsSectionRelation(
                                level = RewardListCardData(
                                    name = prevLevelReward.displayName,
                                    levelUuid = prevLevel.uuid,
                                    type = prevLevelReward.type,
                                    levelName = prevLevel.name,
                                    contractName = state.contract.displayName,
                                    rewardCount = prevLevel.rewards.count(),
                                    amount = prevLevelReward.amount,
                                    useXP = true,
                                    xpTotal = prevLevel.xp,
                                    displayIcon = prevLevelReward.displayIcon,
                                    background = prevLevelReward.background,
                                ),
                                name = "Previous",
                                icon = Icons.AutoMirrored.Default.Undo
                            ),
                            if (nextLevel == null || nextLevelReward == null || isNextLevelOwned == null) null else RelationsSectionRelation(
                                level = RewardListCardData(
                                    name = nextLevelReward.displayName,
                                    levelUuid = nextLevel.uuid,
                                    type = nextLevelReward.type,
                                    levelName = nextLevel.name,
                                    contractName = state.contract.displayName,
                                    rewardCount = nextLevel.rewards.count(),
                                    amount = nextLevelReward.amount,
                                    useXP = true,
                                    xpTotal = nextLevel.xp,
                                    displayIcon = nextLevelReward.displayIcon,
                                    background = nextLevelReward.background,
                                ),
                                name = "Next",
                                icon = Icons.AutoMirrored.Default.Redo
                            )
                        )
                    )

                val relationsSectionUserData =
                    if (state.isLevelRelationsLoading || state.contract == null) null else RelationsSectionUserData(
                        relations = listOfNotNull(
                            if (prevLevel == null || prevLevelReward == null || isPrevLevelOwned == null) null else RelationsSectionState(
                                xpCollected = 0, // TODO: Replace with actual user values
                                isLocked = false, // TODO: Replace with proper isLocked state
                                isOwned = isPrevLevelOwned,
                            ),
                            if (nextLevel == null || nextLevelReward == null || isNextLevelOwned == null) null else RelationsSectionState(
                                xpCollected = 0, // TODO: Replace with actual user values
                                isLocked = false, // TODO: Replace with proper isLocked state
                                isOwned = isNextLevelOwned,
                            )
                        )
                    )

                RelationsSection(
                    contentData = relationsSectionContentData,
                    userData = relationsSectionUserData,
                    onNavToLevelDetails = { levelUuid ->
                        onNavToLevelDetails(
                            levelUuid,
                            state.contract?.uuid ?: ""
                        )
                    }
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.level?.uuid ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        },
        dropdown = {
            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    enabled = isLocked?.not() == true,
                    text = { Text(text = "Reset") },
                    onClick = {
                        isRewardResetAlertShown = true
                        isMenuExpanded = false
                    }
                )
            }
        },

        onOpenDropdown = { isMenuExpanded = true },
        onNavBack = onNavBack,
    )

    if (isRewardUnlockAlertShown && state.userContract?.levels?.any { it.level == state.level?.uuid } == false) {
        val stagedLevels = Level.reverseTraverse(
            contractLevels ?: emptyList(),
            state.level?.uuid,
            state.userContract.levels.lastOrNull()?.level,
            false
        )

        LevelUnlockAlertDialog(
            levels = stagedLevels,
            currency = state.unlockCurrency,
            onDismiss = { isRewardUnlockAlertShown = false },
            onConfirm = {
                stagedLevels.forEach {
                    unlockLevel(it.uuid, true)
                }
            }
        )
    }

    if (isRewardResetAlertShown && state.userContract?.levels?.any { it.level == state.level?.uuid } == true) {
        val stagedLevels = Level.reverseTraverse(
            contractLevels ?: emptyList(),
            state.userContract.levels.lastOrNull()?.level,
            state.level?.uuid,
            true
        )

        LevelResetAlertDialog(
            levels = stagedLevels,
            currency = state.unlockCurrency,
            onDismiss = { isRewardResetAlertShown = false },
            onConfirm = {
                stagedLevels.forEach {
                    resetLevel(it.uuid)
                }
            }
        )
    }
}

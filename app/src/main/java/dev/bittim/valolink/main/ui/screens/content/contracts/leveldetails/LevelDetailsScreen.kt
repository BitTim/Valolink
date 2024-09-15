package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Level
import dev.bittim.valolink.main.ui.components.BaseDetailsScreen
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.components.AgentRewardListCardData
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.dialogs.RewardResetAlertDialog
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.dialogs.RewardUnlockAlertDialog
import dev.bittim.valolink.main.ui.screens.content.contracts.components.LevelBackdrop
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components.LevelHeader
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components.LevelHeaderData
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components.RelationsSection
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components.RelationsSectionData
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components.RelationsSectionRelation
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components.RewardSelector
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components.VariantPreviewCluster

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

    val rewards = state.level?.rewards?.map { it.relation }
    val contractLevels = state.contract?.content?.chapters?.flatMap { it.levels }
    val userContract = state.userData?.contracts?.find { it.contract == state.contract?.uuid }
    if (userContract == null) {
        if (!state.isUserDataLoading) {
            initUserContract()
        }
    }

    val isLocked = userContract?.levels?.any { it.level == state.level?.uuid }?.not()

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

    BaseDetailsScreen(
        isLoading = state.isLevelLoading,

        cardBackground = {
            LevelBackdrop(
                isDisabled = false,
                backgroundImage = reward?.background
            ) {}
        },
        cardImage = {
            Box {
                Crossfade(
                    modifier = Modifier.animateContentSize(),
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
                            .data(reward?.previewImages?.getOrNull(it)?.first ?: "")
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Crossfade(
                    rewards,
                    label = "RewardSelector appearance"
                ) { checkedRewards ->
                    if (checkedRewards != null && checkedRewards.count() > 1) {
                        RewardSelector(
                            displayIcons = checkedRewards.map { it?.displayIcon ?: "" },
                            onRewardSelected = {
                                selectedVariant = 0
                                selectedReward = it
                            }
                        )
                    } else {
                        Spacer(Modifier.width(1.dp))
                    }
                }

                Crossfade(
                    reward?.previewImages?.map { it.second } ?: emptyList(),
                    label = "VariantCluster appearance"
                ) { checkedVariants ->
                    if (checkedVariants.count() > 1) {
                        VariantPreviewCluster(
                            variants = checkedVariants,
                            onSelected = { selectedVariant = it }
                        )
                    } else {
                        Spacer(Modifier.width(1.dp))
                    }
                }
            }
        },
        content = {
            Column {
                val checkedReward = reward

                LevelHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    data = if (
                        checkedReward == null ||
                        state.contract == null ||
                        isLocked == null
                    ) {
                        null
                    } else {
                        LevelHeaderData(
                            displayName = if (checkedReward.amount > 1) "${checkedReward.amount} ${checkedReward.displayName}" else checkedReward.displayName,
                            type = checkedReward.type,
                            displayIcon = checkedReward.displayIcon,
                            levelName = state.level?.name ?: "",
                            contractName = state.contract.displayName,
                            currencyIcon = state.unlockCurrency?.displayIcon,
                            price = state.price,
                            xpTotal = if (state.isGear) -1 else state.level?.xp ?: 0,
                            xpProgress = 25, // TODO: Replace with actual user values,
                            isLocked = false, // TODO: Replace with proper isLocked state
                            isOwned = !isLocked
                        )
                    },
                    onUnlock = {
                        if (userContract?.levels?.lastOrNull()?.level == state.level?.dependency) {
                            // Unlock just one
                            unlockLevel(state.level?.uuid, true)
                        } else {
                            // Unlock multiple at the same time
                            isRewardUnlockAlertShown = true
                        }
                    }
                )

                val prevLevel = state.previousLevel
                val prevLevelReward = prevLevel?.rewards?.find { !it.isFreeReward }?.relation
                val isPrevLevelOwned = userContract?.levels?.any { it.level == prevLevel?.uuid }

                val nextLevel = state.nextLevel
                val nextLevelReward = nextLevel?.rewards?.find { !it.isFreeReward }?.relation
                val isNextLevelOwned = userContract?.levels?.any { it.level == nextLevel?.uuid }

                val relationsSectionData =
                    if (state.isLevelRelationsLoading || state.contract == null) null else RelationsSectionData(
                        relations = listOfNotNull(
                            if (prevLevel == null || prevLevelReward == null || isPrevLevelOwned == null) null else RelationsSectionRelation(
                                level = AgentRewardListCardData(
                                    name = prevLevelReward.displayName,
                                    levelUuid = prevLevel.uuid,
                                    type = prevLevelReward.type,
                                    levelName = prevLevel.name,
                                    contractName = state.contract.displayName,
                                    amount = prevLevelReward.amount,
                                    displayIcon = prevLevelReward.displayIcon,
                                    background = prevLevelReward.background,
                                    isLocked = false, // TODO: Replace with proper isLocked state
                                    isOwned = isPrevLevelOwned,
                                ),
                                name = "Previous",
                                icon = Icons.AutoMirrored.Default.Undo
                            ),
                            if (nextLevel == null || nextLevelReward == null || isNextLevelOwned == null) null else RelationsSectionRelation(
                                level = AgentRewardListCardData(
                                    name = nextLevelReward.displayName,
                                    levelUuid = nextLevel.uuid,
                                    type = nextLevelReward.type,
                                    levelName = nextLevel.name,
                                    contractName = state.contract.displayName,
                                    amount = nextLevelReward.amount,
                                    displayIcon = nextLevelReward.displayIcon,
                                    background = nextLevelReward.background,
                                    isLocked = false, // TODO: Replace with proper isLocked state
                                    isOwned = isNextLevelOwned,
                                ),
                                name = "Next",
                                icon = Icons.AutoMirrored.Default.Redo
                            )
                        )
                    )

                RelationsSection(
                    data = relationsSectionData,
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
                    enabled = isLocked?.not() ?: false,
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

    if (isRewardUnlockAlertShown && userContract?.levels?.any { it.level == state.level?.uuid } == false) {
        val stagedLevels = Level.reverseTraverse(
            contractLevels ?: emptyList(),
            state.level?.uuid,
            userContract.levels.lastOrNull()?.level,
            false
        )

        RewardUnlockAlertDialog(
            levels = stagedLevels,
            currencyDisplayIcon = state.unlockCurrency?.displayIcon,
            onDismiss = { isRewardUnlockAlertShown = false },
            onConfirm = {
                stagedLevels.forEach {
                    unlockLevel(it.uuid, true)
                }
            }
        )
    }

    if (isRewardResetAlertShown && userContract?.levels?.any { it.level == state.level?.uuid } == true) {
        val stagedLevels = Level.reverseTraverse(
            contractLevels ?: emptyList(),
            userContract.levels.lastOrNull()?.level,
            state.level?.uuid,
            true
        )

        RewardResetAlertDialog(
            levels = stagedLevels,
            currencyDisplayIcon = state.unlockCurrency?.displayIcon,
            onDismiss = { isRewardResetAlertShown = false },
            onConfirm = {
                stagedLevels.forEach {
                    resetLevel(it.uuid)
                }
            }
        )
    }
}
package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.dialogs.RewardResetAlertDialog
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.dialogs.RewardUnlockAlertDialog
import dev.bittim.valolink.main.ui.screens.content.contracts.components.LevelBackdrop
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components.LevelHeader
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components.LevelHeaderData
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
) {
    // Fetch details if they haven't been fetched yet
    if (state.level == null || state.contract == null) {
        if (!state.isLevelLoading && !state.isContractLoading) {
            fetchDetails(uuid, contract)
        }
    }

    // --------------------------------
    //  Logic
    // --------------------------------

    val reward = state.level?.reward?.relation
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

    var selectedVariant by remember {
        mutableIntStateOf(0)
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
                            .data(reward?.previewImages?.get(it)?.first ?: "")
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
            VariantPreviewCluster(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                variants = reward?.previewImages?.map { it.second } ?: emptyList(),
                onSelected = { selectedVariant = it }
            )
        },
        content = {
            Column {
                LevelHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    data = if (
                        reward == null ||
                        state.contract == null ||
                        state.unlockCurrency == null ||
                        isLocked == null
                    ) {
                        null
                    } else {
                        LevelHeaderData(
                            displayName = if (reward.amount > 1) "${reward.amount} ${reward.displayName}" else reward.displayName,
                            type = reward.type,
                            displayIcon = reward.displayIcon,
                            levelName = state.level.name,
                            contractName = state.contract.displayName,
                            currencyIcon = state.unlockCurrency.displayIcon,
                            price = state.price,
                            xpTotal = if (state.isGear) -1 else state.level.xp,
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
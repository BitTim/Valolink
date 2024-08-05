package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.main.domain.model.game.agent.Agent
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Level
import dev.bittim.valolink.main.ui.components.BaseDetailsScreen
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.components.conditional
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.components.AbilityDetailsItem
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.components.AgentRewardCard
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.dialogs.AgentResetAlertDialog
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.dialogs.RewardResetAlertDialog
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.dialogs.RewardUnlockAlertDialog
import dev.bittim.valolink.main.ui.screens.content.contracts.components.AgentBackdrop
import dev.bittim.valolink.main.ui.util.getProgressPercent

@Composable
fun AgentDetailsScreen(
    state: AgentDetailsState,
    uuid: String,
    fetchDetails: (uuid: String) -> Unit,
    unlockAgent: () -> Unit,
    resetAgent: () -> Unit,
    initUserContract: () -> Unit,
    unlockLevel: (uuid: String) -> Unit,
    resetLevel: (uuid: String) -> Unit,
    onAbilityTabChanged: (index: Int) -> Unit,
    onNavBack: () -> Unit,
    onNavGearRewardsList: () -> Unit,
    onNavToLevelDetails: (level: String, contract: String) -> Unit,
) {
    // Fetch details if they haven't been fetched yet
    if (state.agentGear == null) {
        LaunchedEffect(Unit) {
            fetchDetails(uuid)
        }
    }

    if (state.agentGear != null && state.agentGear.content.relation is Agent) {
        // --------------------------------
        //  Logic
        // --------------------------------

        val agent = state.agentGear.content.relation
        val isLocked = !(state.userData?.agents?.any { it.agent == agent.uuid } ?: false)

        var isMenuExpanded: Boolean by remember { mutableStateOf(false) }
        var isAgentResetAlertShown: Boolean by remember { mutableStateOf(false) }
        var isRewardUnlockAlertShown: Boolean by remember { mutableStateOf(false) }
        var isRewardResetAlertShown: Boolean by remember { mutableStateOf(false) }

        var targetLevelUuid: String by remember { mutableStateOf("") }

        val agentGearLevels = state.agentGear.content.chapters.flatMap { it.levels }
        val userContract = state.userData?.contracts?.find {
            it.contract == state.agentGear.uuid
        }

        if (userContract == null) {
            initUserContract()
            return
        }

        val abilityPagerState = rememberPagerState {
            agent.abilities.count()
        }

        LaunchedEffect(state.selectedAbility) {
            abilityPagerState.animateScrollToPage(state.selectedAbility)
        }

        LaunchedEffect(
            abilityPagerState.currentPage
        ) {
            onAbilityTabChanged(abilityPagerState.currentPage)
        }

        // Scroll to the currently active reward
        LaunchedEffect(
            userContract.levels.count(),
            state.agentGear
        ) {
            val targetIndex = agentGearLevels.indexOfFirst {
                it.uuid == userContract.levels.lastOrNull()?.uuid
            }

            if (targetIndex > -1) state.rewardListState.animateScrollToItem(targetIndex)
        }

        // --------------------------------
        //  Layout
        // --------------------------------

        BaseDetailsScreen(
            isLoading = false,

            cardBackground = {
                AgentBackdrop(
                    modifier = Modifier.fillMaxSize(),
                    useGradient = true,
                    backgroundGradientColors = agent.backgroundGradientColors,
                    backgroundImage = agent.background,
                    isDisabled = isLocked
                ) {}
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
                    model = agent.fullPortrait,
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
                                text = agent.displayName,
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
                                    model = agent.role.displayIcon,
                                    contentDescription = null,
                                    placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_role_icon)
                                )
                                Text(
                                    text = agent.role.displayName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                        val totalLevels = state.agentGear.calcLevelCount()
                        val unlockedLevels = userContract.levels.count()
                        val percentage = getProgressPercent(unlockedLevels, totalLevels)

                        val animatedProgress: Float by animateFloatAsState(
                            targetValue = (percentage / 100f),
                            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                            label = ""
                        )

                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(),
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
                                text = agent.displayName,
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
                                    model = agent.role.displayIcon,
                                    contentDescription = null,
                                    placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_role_icon)
                                )
                                Text(
                                    text = agent.role.displayName,
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
                                text = "8000"
                            )
                        }
                    }
                }
            },
            content = {
                Column {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rewards",
                            style = MaterialTheme.typography.titleLarge
                        )

                        IconButton(onClick = onNavGearRewardsList) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                                contentDescription = null
                            )
                        }
                    }

                    if (isLocked) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = "Unlock the agent to access rewards",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        state = state.rewardListState,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.agentGear.content.chapters.flatMap { chapter -> chapter.levels },
                            itemContent = { level ->
                                val reward = level.reward.relation

                                if (reward != null) {
                                    AgentRewardCard(
                                        name = reward.displayName,
                                        levelUuid = level.uuid,
                                        type = reward.type,
                                        previewIcon = reward.previewImages.first().first ?: "",
                                        background = reward.background,
                                        price = level.doughCost,
                                        amount = reward.amount,
                                        currencyIcon = state.dough?.displayIcon ?: "",
                                        isLocked = isLocked,
                                        isOwned = userContract.levels.any { it.level == level.uuid },
                                        unlockReward = {
                                            if (userContract.levels.lastOrNull()?.level == level.dependency) {
                                                // Unlock just one
                                                unlockLevel(level.uuid)
                                            } else {
                                                // Unlock multiple at the same time
                                                targetLevelUuid = level.uuid
                                                isRewardUnlockAlertShown = true
                                            }
                                        },
                                        resetReward = {
                                            targetLevelUuid = level.uuid
                                            isRewardResetAlertShown = true
                                        },
                                        onNavToLevelDetails = { levelUuid ->
                                            onNavToLevelDetails(levelUuid, state.agentGear.uuid)
                                        }
                                    )
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Abilities",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TabRow(selectedTabIndex = state.selectedAbility) {
                        agent.abilities.forEachIndexed { index, ability ->
                            val isSelected = index == state.selectedAbility

                            Tab(selected = isSelected,
                                onClick = { onAbilityTabChanged(index) },
                                icon = {
                                    AsyncImage(
                                        modifier = Modifier.padding(12.dp),
                                        model = ability.displayIcon,
                                        contentDescription = ability.displayName,
                                        colorFilter = ColorFilter.tint(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant),
                                        placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_ability_icon)
                                    )
                                })
                        }
                    }

                    HorizontalPager(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        state = abilityPagerState
                    ) { index ->
                        val ability = agent.abilities[index]

                        Card(
                            modifier = Modifier.padding(16.dp),
                        ) {
                            AbilityDetailsItem(
                                modifier = Modifier.padding(16.dp),
                                displayName = ability.displayName,
                                slot = ability.slot,
                                description = ability.description,
                                displayIcon = ability.displayIcon
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Details",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = agent.displayName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = agent.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .height(16.dp)
                                    .aspectRatio(1f),
                                model = agent.role.displayIcon,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_role_icon)
                            )
                            Text(
                                text = agent.role.displayName,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Text(
                            text = agent.role.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Contract: ${state.agentGear.uuid}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )

                        Text(
                            text = "Agent: ${agent.uuid}",
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
            AgentResetAlertDialog(
                agentName = agent.displayName,
                onDismiss = { isAgentResetAlertShown = false },
                onConfirm = resetAgent
            )
        }

        if (isRewardUnlockAlertShown && !userContract.levels.any { it.level == targetLevelUuid }) {
            val stagedLevels = Level.reverseTraverse(
                agentGearLevels,
                targetLevelUuid,
                userContract.levels.lastOrNull()?.level,
                false
            )

            RewardUnlockAlertDialog(
                levels = stagedLevels,
                currencyDisplayIcon = state.dough?.displayIcon,
                onDismiss = { isRewardUnlockAlertShown = false },
                onConfirm = {
                    stagedLevels.forEach {
                        unlockLevel(it.uuid)
                    }
                }
            )
        }

        if (isRewardResetAlertShown && userContract.levels.any { it.level == targetLevelUuid }) {
            val stagedLevels = Level.reverseTraverse(
                agentGearLevels,
                userContract.levels.lastOrNull()?.level,
                targetLevelUuid,
                true
            )

            RewardResetAlertDialog(
                levels = stagedLevels,
                currencyDisplayIcon = state.dough?.displayIcon,
                onDismiss = { isRewardResetAlertShown = false },
                onConfirm = {
                    stagedLevels.forEach {
                        resetLevel(it.uuid)
                    }
                }
            )
        }
    }
}
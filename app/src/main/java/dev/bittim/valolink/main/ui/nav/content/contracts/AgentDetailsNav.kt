/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       AgentDetailsNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   18.12.24, 02:29
 */

package dev.bittim.valolink.main.ui.nav.content.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.AgentDetailsScreen
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.AgentDetailsViewModel
import kotlinx.serialization.Serializable

@Serializable
data class AgentDetailsNav(
    val uuid: String,
)

fun NavGraphBuilder.contractsAgentDetailsScreen(
    onNavBack: () -> Unit,
    onNavLevelList: (uuid: String) -> Unit,
    onNavLevelDetails: (levelUuid: String, contractUuid: String) -> Unit,
) {
    composable<AgentDetailsNav>(
        enterTransition = { Transition.ForwardBackward.enter },
        exitTransition = { Transition.ForwardBackward.exit },
        popEnterTransition = { Transition.ForwardBackward.popEnter },
        popExitTransition = { Transition.ForwardBackward.popExit },
    ) {
        val viewModel: AgentDetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val args = it.toRoute<AgentDetailsNav>()

        AgentDetailsScreen(
            state = state,
            uuid = args.uuid,
            fetchDetails = viewModel::fetchDetails,
            unlockAgent = viewModel::unlockAgent,
            resetAgent = viewModel::resetAgent,
            initUserContract = viewModel::initUserContract,
            unlockLevel = viewModel::unlockLevel,
            resetLevel = viewModel::resetLevel,
            onNavBack = onNavBack,
            onNavLevelList = onNavLevelList,
            onNavLevelDetails = onNavLevelDetails
        )
    }
}

fun NavController.navToContractsAgentDetails(uuid: String) {
    navigate(AgentDetailsNav(uuid)) {
        launchSingleTop = true
    }
}
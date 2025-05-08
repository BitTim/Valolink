/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AgentListNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:16
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.agentlist

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.core.ui.theme.Transition
import kotlinx.serialization.Serializable

@Serializable
object AgentListNav

fun NavGraphBuilder.contractsGearListScreen(
    onNavBack: () -> Unit,
    onNavToAgentDetails: (String) -> Unit,
) {
    composable<AgentListNav>(
        enterTransition = { Transition.ForwardBackward.enter },
        exitTransition = { Transition.ForwardBackward.exit },
        popEnterTransition = { Transition.ForwardBackward.popEnter },
        popExitTransition = { Transition.ForwardBackward.popExit },
    ) {
        val viewModel: AgentListViewModel = hiltViewModel()
        val gearListState by viewModel.state.collectAsStateWithLifecycle()

        AgentListScreen(
            state = gearListState,
            initUserContract = viewModel::initUserContract,
            onNavBack = onNavBack,
            onNavToAgentDetails = onNavToAgentDetails
        )
    }
}

fun NavController.navToContractsGearList() {
    navigate(AgentListNav) {
        launchSingleTop = true
    }
}

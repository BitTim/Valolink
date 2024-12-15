/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       AgentListNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.main.ui.nav.content.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.main.ui.screens.content.contracts.agentlist.AgentListScreen
import dev.bittim.valolink.main.ui.screens.content.contracts.agentlist.AgentListViewModel
import kotlinx.serialization.Serializable

@Serializable
object AgentListNav

fun NavGraphBuilder.contractsGearListScreen(
    onNavBack: () -> Unit,
    onNavToAgentDetails: (String) -> Unit,
) {
    composable<AgentListNav>(enterTransition = { Transition.forward },
                             popExitTransition = { Transition.backward }) {
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
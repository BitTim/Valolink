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
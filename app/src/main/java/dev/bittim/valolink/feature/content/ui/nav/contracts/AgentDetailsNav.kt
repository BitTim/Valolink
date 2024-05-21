package dev.bittim.valolink.feature.content.ui.nav.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.bittim.valolink.feature.content.ui.screens.contracts.agentdetails.AgentDetailsScreen
import dev.bittim.valolink.feature.content.ui.screens.contracts.agentdetails.AgentDetailsViewModel
import dev.bittim.valolink.ui.theme.Transition
import kotlinx.serialization.Serializable

@Serializable
data class AgentDetailsNav(
    val uuid: String
)

fun NavGraphBuilder.contractsAgentDetailsScreen(
    onNavBack: () -> Unit
) {
    composable<AgentDetailsNav>(
        enterTransition = { Transition.forward },
        popExitTransition = { Transition.backward }
    ) {
        val viewModel: AgentDetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val args = it.toRoute<AgentDetailsNav>()
        viewModel.fetchDetails(args.uuid)

        AgentDetailsScreen(
            state = state,
            onAbilityTabChanged = viewModel::onAbilityChanged,
            onNavBack = onNavBack,
            onNavGearRewardsList = {}
        )
    }
}

fun NavController.navToContractsAgentDetails(uuid: String) {
    navigate(AgentDetailsNav(uuid)) {
        launchSingleTop = true
    }
}
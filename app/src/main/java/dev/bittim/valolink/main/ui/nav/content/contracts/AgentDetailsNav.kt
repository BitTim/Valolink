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
    onNavToLevelDetails: (String) -> Unit,
) {
    composable<AgentDetailsNav>(enterTransition = { Transition.forward },
                                popExitTransition = { Transition.backward }) {
        val viewModel: AgentDetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val args = it.toRoute<AgentDetailsNav>()
        viewModel.fetchDetails(args.uuid)

        AgentDetailsScreen(
            state = state,
            unlockAgent = viewModel::unlockAgent,
            resetAgent = viewModel::resetAgent,
            initUserContract = viewModel::initUserContract,
            unlockLevel = viewModel::unlockLevel,
            resetLevel = viewModel::resetLevel,
            onAbilityTabChanged = viewModel::onAbilityChanged,
            onNavBack = onNavBack,
            onNavGearRewardsList = {},
            onNavToLevelDetails = onNavToLevelDetails
        )
    }
}

fun NavController.navToContractsAgentDetails(uuid: String) {
    navigate(AgentDetailsNav(uuid)) {
        launchSingleTop = true
    }
}
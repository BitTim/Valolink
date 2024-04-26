package dev.bittim.valolink.feature.content.ui.contracts.agentdetails

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.bittim.valolink.ui.theme.Transition

const val ContractsAgentDetailsNavRoute = "agentDetails"

fun NavGraphBuilder.contractsAgentDetailsScreen(
    onNavBack: () -> Unit
) {
    composable(
        route = "$ContractsAgentDetailsNavRoute/{uuid}",
        arguments = listOf(navArgument("uuid") { type = NavType.StringType }),
        enterTransition = { Transition.forward },
        popExitTransition = { Transition.backward }
    ) {
        val viewModel: AgentDetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val uuid = it.arguments?.getString("uuid")
        viewModel.fetchDetails(uuid)

        AgentDetailsScreen(
            state = state,
            onAbilityTabChanged = viewModel::onAbilityChanged,
            onNavBack = onNavBack,
            onNavGearRewardsList = {}
        )
    }
}

fun NavController.navToContractsAgentDetails(uuid: String) {
    navigate("$ContractsAgentDetailsNavRoute/$uuid") {
        launchSingleTop = true
    }
}
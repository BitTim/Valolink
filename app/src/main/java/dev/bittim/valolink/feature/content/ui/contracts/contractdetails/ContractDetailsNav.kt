package dev.bittim.valolink.feature.content.ui.contracts.contractdetails

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.bittim.valolink.ui.theme.Transition

const val ContractsContractDetailsNavRoute = "contractDetails"

fun NavGraphBuilder.contractsContractDetailsScreen(
    onNavBack: () -> Unit
) {
    composable(route = "$ContractsContractDetailsNavRoute/{uuid}",
        arguments = listOf(navArgument("uuid") { type = NavType.StringType }),
        enterTransition = { Transition.forward },
        popExitTransition = { Transition.backward }) {
        val viewModel: ContractDetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val uuid = it.arguments?.getString("uuid")
        viewModel.fetchDetails(uuid)

        ContractDetailsScreen(state = state, onNavBack = onNavBack, onNavContractRewardsList = {})
    }
}

fun NavController.navToContractsContractDetails(uuid: String) {
    navigate("$ContractsContractDetailsNavRoute/$uuid") {
        launchSingleTop = true
    }
}
package dev.bittim.valolink.feature.content.ui.nav.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.feature.content.ui.screens.contracts.main.ContractsMainScreen
import dev.bittim.valolink.feature.content.ui.screens.contracts.main.ContractsMainViewModel
import dev.bittim.valolink.ui.theme.Transition
import kotlinx.serialization.Serializable

@Serializable
object ContractsMainNav

fun NavGraphBuilder.contractsMainScreen(
    onNavToGearList: () -> Unit,
    onNavToAgentDetails: (String) -> Unit,
    onNavToContractDetails: (String) -> Unit
) {
    composable<ContractsMainNav>(
        enterTransition = { Transition.topLevelEnter },
        exitTransition = { Transition.topLevelExit },
        popEnterTransition = { Transition.topLevelEnter },
        popExitTransition = { Transition.topLevelExit }) {
        val viewModel: ContractsMainViewModel = hiltViewModel()
        val contractsMainState by viewModel.state.collectAsStateWithLifecycle()

        ContractsMainScreen(
            state = contractsMainState,
            onArchiveTypeFilterChange = viewModel::onArchiveTypeFilterChange,
            onNavToGearList = onNavToGearList,
            onNavToAgentDetails = onNavToAgentDetails,
            onNavToContractDetails = onNavToContractDetails
        )
    }
}

fun NavController.navToContractsMain() {
    navigate(ContractsMainNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
package dev.bittim.valolink.main.ui.nav.content.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.main.ui.screens.content.contracts.overview.ContractsOverviewScreen
import dev.bittim.valolink.main.ui.screens.content.contracts.overview.ContractsOverviewViewModel
import kotlinx.serialization.Serializable

@Serializable
object ContractsMainNav

fun NavGraphBuilder.contractsMainScreen(
    onNavToGearList: () -> Unit,
    onNavToAgentDetails: (String) -> Unit,
    onNavToContractDetails: (String) -> Unit,
) {
    composable<ContractsMainNav>(enterTransition = { Transition.topLevelEnter },
                                 exitTransition = { Transition.topLevelExit },
                                 popEnterTransition = { Transition.topLevelEnter },
                                 popExitTransition = { Transition.topLevelExit }) {
        val viewModel: ContractsOverviewViewModel = hiltViewModel()
        val contractsMainState by viewModel.state.collectAsStateWithLifecycle()

        ContractsOverviewScreen(
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
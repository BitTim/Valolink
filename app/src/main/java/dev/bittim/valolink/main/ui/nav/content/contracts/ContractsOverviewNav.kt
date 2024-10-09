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
object ContractsOverviewNav

fun NavGraphBuilder.contractsOverviewScreen(
    onNavToGearList: () -> Unit,
    onNavToAgentDetails: (String) -> Unit,
    onNavToContractDetails: (String) -> Unit,
) {
    composable<ContractsOverviewNav>(enterTransition = { Transition.topLevelEnter },
                                     exitTransition = { Transition.topLevelExit },
                                     popEnterTransition = { Transition.topLevelEnter },
                                     popExitTransition = { Transition.topLevelExit }) {
        val viewModel: ContractsOverviewViewModel = hiltViewModel()
        val contractsMainState by viewModel.state.collectAsStateWithLifecycle()

        ContractsOverviewScreen(
            state = contractsMainState,
            initUserContract = viewModel::initUserContract,
            onArchiveTypeFilterChange = viewModel::onArchiveTypeFilterChange,
            onNavToGearList = onNavToGearList,
            onNavToAgentDetails = onNavToAgentDetails,
            onNavToContractDetails = onNavToContractDetails
        )
    }
}

fun NavController.navToContractsOverview() {
    navigate(ContractsOverviewNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
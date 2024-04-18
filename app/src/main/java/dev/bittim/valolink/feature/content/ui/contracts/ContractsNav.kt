package dev.bittim.valolink.feature.content.ui.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.ui.theme.Transition

const val ContractsNavRoute = "contracts"

fun NavGraphBuilder.contractsScreen() {
    composable(
        route = ContractsNavRoute,
        enterTransition = { Transition.topLevelEnter },
        exitTransition = { Transition.topLevelExit },
        popEnterTransition = { Transition.topLevelEnter },
        popExitTransition = { Transition.topLevelExit }
    ) {
        val viewModel: ContractsViewModel = hiltViewModel()
        val contractsState by viewModel.state.collectAsStateWithLifecycle()

        ContractsScreen(
            state = contractsState, onArchiveTypeFilterChange = viewModel::onArchiveTypeFilterChange
        )
    }
}

fun NavController.navToContracts(origin: String) {
    navigate(ContractsNavRoute) {
        popUpTo(origin) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
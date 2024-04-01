package dev.bittim.valolink.feature.content.ui.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val ContractsNavRoute = "contracts"

fun NavGraphBuilder.contractsScreen() {
    composable(ContractsNavRoute) {
        val viewModel: ContractsViewModel = hiltViewModel()
        val contractsState by viewModel.contractsState.collectAsStateWithLifecycle()

        ContractsScreen(
            state = contractsState,
            onFetch = viewModel::onFetch
        )
    }
}

fun NavController.navToContracts(origin: String) {
    navigate(ContractsNavRoute) {
        popUpTo(origin) {
            inclusive = true
        }
        launchSingleTop = true
    }
}
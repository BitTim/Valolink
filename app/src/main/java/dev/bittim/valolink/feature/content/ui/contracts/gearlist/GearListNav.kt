package dev.bittim.valolink.feature.content.ui.contracts.gearlist

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.ui.theme.Transition

const val ContractsGearListNavRoute = "gearlist"

fun NavGraphBuilder.contractsGearListScreen(
    onNavBack: () -> Unit
) {
    composable(route = ContractsGearListNavRoute,
        enterTransition = { Transition.forward },
        popExitTransition = { Transition.backward }) {
        val viewModel: GearListViewModel = hiltViewModel()
        val gearListState by viewModel.state.collectAsStateWithLifecycle()

        GearListScreen(
            state = gearListState, onNavBack = onNavBack
        )
    }
}

fun NavController.navToContractsGearList() {
    navigate(ContractsGearListNavRoute) {
        launchSingleTop = true
    }
}
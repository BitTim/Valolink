package dev.bittim.valolink.feature.main.ui.nav.content.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.bittim.valolink.feature.main.ui.screens.content.contracts.contractdetails.ContractDetailsScreen
import dev.bittim.valolink.feature.main.ui.screens.content.contracts.contractdetails.ContractDetailsViewModel
import dev.bittim.valolink.ui.theme.Transition
import kotlinx.serialization.Serializable

@Serializable
data class ContractDetailsNav(
    val uuid: String,
)

fun NavGraphBuilder.contractsContractDetailsScreen(
    onNavBack: () -> Unit,
) {
    composable<ContractDetailsNav>(enterTransition = { Transition.forward },
                                   popExitTransition = { Transition.backward }) {
        val viewModel: ContractDetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val args = it.toRoute<ContractDetailsNav>()
        viewModel.fetchDetails(args.uuid)

        ContractDetailsScreen(state = state,
                              onNavBack = onNavBack,
                              onNavContractRewardsList = {})
    }
}

fun NavController.navToContractsContractDetails(uuid: String) {
    navigate(ContractDetailsNav(uuid)) {
        launchSingleTop = true
    }
}
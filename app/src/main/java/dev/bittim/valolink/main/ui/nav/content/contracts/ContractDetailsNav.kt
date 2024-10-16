package dev.bittim.valolink.main.ui.nav.content.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.main.ui.screens.content.contracts.contractdetails.ContractDetailsScreen
import dev.bittim.valolink.main.ui.screens.content.contracts.contractdetails.ContractDetailsViewModel
import kotlinx.serialization.Serializable

@Serializable
data class ContractDetailsNav(
    val uuid: String,
)

fun NavGraphBuilder.contractsContractDetailsScreen(
    onNavBack: () -> Unit,
    onNavLevelList: (uuid: String) -> Unit,
    onNavToAgentDetails: (uuid: String) -> Unit,
    onNavToLevelDetails: (levelUuid: String, contractUuid: String) -> Unit,
) {
    composable<ContractDetailsNav>(enterTransition = { Transition.forward },
                                   popExitTransition = { Transition.backward }) {
        val viewModel: ContractDetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val args = it.toRoute<ContractDetailsNav>()

        ContractDetailsScreen(
            state = state,
            uuid = args.uuid,
            fetchDetails = viewModel::fetchDetails,
            onNavBack = onNavBack,
            onNavLevelList = onNavLevelList,
            onNavToAgentDetails = onNavToAgentDetails,
            onNavToLevelDetails = onNavToLevelDetails
        )
    }
}

fun NavController.navToContractsContractDetails(uuid: String) {
    navigate(ContractDetailsNav(uuid)) {
        launchSingleTop = true
    }
}
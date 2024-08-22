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
    val isRecruitment: Boolean,
)

fun NavGraphBuilder.contractsContractDetailsScreen(
    onNavBack: () -> Unit,
    onNavToAgentDetails: (String) -> Unit,
    onNavToLevelDetails: (String, String) -> Unit,
) {
    composable<ContractDetailsNav>(enterTransition = { Transition.forward },
                                   popExitTransition = { Transition.backward }) {
        val viewModel: ContractDetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val args = it.toRoute<ContractDetailsNav>()

        ContractDetailsScreen(
            state = state,
            uuid = args.uuid,
            isRecruitment = args.isRecruitment,
            fetchDetails = viewModel::fetchDetails,
            onNavBack = onNavBack,
            onNavContractRewardsList = {},
            onNavToAgentDetails = onNavToAgentDetails,
            onNavToLevelDetails = onNavToLevelDetails
        )
    }
}

fun NavController.navToContractsContractDetails(uuid: String, isRecruitment: Boolean) {
    navigate(ContractDetailsNav(uuid, isRecruitment)) {
        launchSingleTop = true
    }
}
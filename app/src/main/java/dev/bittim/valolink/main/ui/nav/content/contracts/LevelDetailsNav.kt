package dev.bittim.valolink.main.ui.nav.content.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.LevelDetailsScreen
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.LevelDetailsViewModel
import kotlinx.serialization.Serializable

@Serializable
data class LevelDetailsNav(
    val uuid: String,
    val contract: String,
)

fun NavGraphBuilder.contractsLevelDetailsScreen(
    onNavBack: () -> Unit,
) {
    composable<LevelDetailsNav>(enterTransition = { Transition.forward },
                                popExitTransition = { Transition.backward }) {
        val viewModel: LevelDetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val args = it.toRoute<LevelDetailsNav>()
        viewModel.fetchDetails(args.uuid, args.contract)

        LevelDetailsScreen(
            state = state,
            initUserContract = viewModel::initUserContract,
            unlockLevel = viewModel::unlockLevel,
            resetLevel = viewModel::resetLevel,
            onNavBack = onNavBack
        )
    }
}

fun NavController.navToContractsLevelDetails(uuid: String, contract: String) {
    navigate(LevelDetailsNav(uuid, contract)) {
        launchSingleTop = true
    }
}
package dev.bittim.valolink.main.ui.nav.content.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.main.ui.screens.content.contracts.levellist.LevelListScreen
import dev.bittim.valolink.main.ui.screens.content.contracts.levellist.LevelListViewModel
import kotlinx.serialization.Serializable

@Serializable
data class LevelListNav(
    val uuid: String,
)

fun NavGraphBuilder.contractsLevelListScreen(
    onNavBack: () -> Unit,
    onNavToAgentDetails: (uuid: String) -> Unit,
    onNavToLevelDetails: (levelUuid: String, contractUuid: String) -> Unit,
) {
    composable<LevelListNav>(enterTransition = { Transition.forward },
                             popExitTransition = { Transition.backward }) {
        val viewModel: LevelListViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val args = it.toRoute<LevelListNav>()

        LevelListScreen(
            state = state,
            uuid = args.uuid,
            fetchDetails = viewModel::fetchDetails,
            resetContract = viewModel::resetContract,
            initUserContract = viewModel::initUserContract,
            resetLevel = viewModel::resetLevel,
            onNavBack = onNavBack,
            onNavToAgentDetails = onNavToAgentDetails,
            onNavToLevelDetails = onNavToLevelDetails
        )
    }
}

fun NavController.navToLevelList(uuid: String) {
    navigate(LevelListNav(uuid)) {
        launchSingleTop = true
    }
}
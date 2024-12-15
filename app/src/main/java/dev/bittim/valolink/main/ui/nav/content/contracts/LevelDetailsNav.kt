/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       LevelDetailsNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.main.ui.nav.content.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
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
    onNavToLevelDetails: (level: String, contract: String) -> Unit,
) {
    composable<LevelDetailsNav>(enterTransition = { Transition.forward },
                                popExitTransition = { Transition.backward }) {
        val viewModel: LevelDetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val args = it.toRoute<LevelDetailsNav>()

        LevelDetailsScreen(
            state = state,
            uuid = args.uuid,
            contract = args.contract,
            fetchDetails = viewModel::fetchDetails,
            initUserContract = viewModel::initUserContract,
            unlockLevel = viewModel::unlockLevel,
            resetLevel = viewModel::resetLevel,
            onNavBack = onNavBack,
            onNavToLevelDetails = onNavToLevelDetails
        )
    }
}

fun NavController.navToContractsLevelDetails(uuid: String, contract: String) {
    if (currentDestination?.hasRoute(LevelDetailsNav::class) == true) {
        popBackStack<LevelDetailsNav>(true)
    }

    navigate(LevelDetailsNav(uuid, contract))
}
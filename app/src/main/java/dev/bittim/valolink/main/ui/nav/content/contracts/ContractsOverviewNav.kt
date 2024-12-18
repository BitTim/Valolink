/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ContractsOverviewNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   18.12.24, 02:29
 */

package dev.bittim.valolink.main.ui.nav.content.contracts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
    composable<ContractsOverviewNav> {
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
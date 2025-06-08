/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractsOverviewNav.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 20:32
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.overview

import android.graphics.Bitmap
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object ContractsOverviewNav

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.contractsOverviewScreen(
    userAvatar: Bitmap?,
    onNavToGearList: () -> Unit,
    onNavToAgentDetails: (String) -> Unit,
    onNavToContractDetails: (String) -> Unit,
) {
    composable<ContractsOverviewNav> {
        val viewModel: ContractsOverviewViewModel = hiltViewModel()
        val contractsMainState by viewModel.state.collectAsStateWithLifecycle()

        ContractsOverviewScreen(
            state = contractsMainState,
            userAvatar = userAvatar,
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

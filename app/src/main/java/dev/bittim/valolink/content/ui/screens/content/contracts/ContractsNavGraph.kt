/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractsNavGraph.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:31
 */

package dev.bittim.valolink.content.ui.screens.content.contracts

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.contractsAgentDetailsScreen
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.navToContractsAgentDetails
import dev.bittim.valolink.content.ui.screens.content.contracts.agentlist.contractsGearListScreen
import dev.bittim.valolink.content.ui.screens.content.contracts.agentlist.navToContractsGearList
import dev.bittim.valolink.content.ui.screens.content.contracts.contractdetails.contractsContractDetailsScreen
import dev.bittim.valolink.content.ui.screens.content.contracts.contractdetails.navToContractsContractDetails
import dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.contractsLevelDetailsScreen
import dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.navToContractsLevelDetails
import dev.bittim.valolink.content.ui.screens.content.contracts.levellist.contractsLevelListScreen
import dev.bittim.valolink.content.ui.screens.content.contracts.levellist.navToLevelList
import dev.bittim.valolink.content.ui.screens.content.contracts.overview.ContractsOverviewNav
import dev.bittim.valolink.content.ui.screens.content.contracts.overview.contractsOverviewScreen
import kotlinx.serialization.Serializable

@Serializable
object ContractsNav

fun NavGraphBuilder.contractsNavGraph(
    navController: NavController,
) {
    navigation<ContractsNav>(
        startDestination = ContractsOverviewNav
    ) {
        contractsOverviewScreen(
            onNavToGearList = { navController.navToContractsGearList() },
            onNavToAgentDetails = { uuid -> navController.navToContractsAgentDetails(uuid) },
            onNavToContractDetails = { uuid ->
                navController.navToContractsContractDetails(uuid)
            }
        )

        contractsGearListScreen(
            onNavBack = { navController.navigateUp() },
            onNavToAgentDetails = { uuid -> navController.navToContractsAgentDetails(uuid) }
        )

        contractsAgentDetailsScreen(
            onNavBack = { navController.navigateUp() },
            onNavLevelList = { uuid -> navController.navToLevelList(uuid) },
            onNavLevelDetails = { uuid, contract ->
                navController.navToContractsLevelDetails(
                    uuid,
                    contract
                )
            }
        )

        contractsContractDetailsScreen(
            onNavBack = { navController.navigateUp() },
            onNavLevelList = { uuid -> navController.navToLevelList(uuid) },
            onNavToAgentDetails = { uuid -> navController.navToContractsAgentDetails(uuid) },
            onNavToLevelDetails = { uuid, contract ->
                navController.navToContractsLevelDetails(
                    uuid,
                    contract
                )
            }
        )

        contractsLevelDetailsScreen(
            onNavBack = { navController.navigateUp() },
            onNavToLevelDetails = { uuid, contract ->
                navController.navToContractsLevelDetails(
                    uuid,
                    contract
                )
            }
        )

        contractsLevelListScreen(
            onNavBack = { navController.navigateUp() },
            onNavToAgentDetails = { uuid -> navController.navToContractsAgentDetails(uuid) },
            onNavToLevelDetails = { uuid, contract ->
                navController.navToContractsLevelDetails(
                    uuid,
                    contract
                )
            }
        )
    }
}

fun NavController.navToContractsGraph() {
    navigate(ContractsNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

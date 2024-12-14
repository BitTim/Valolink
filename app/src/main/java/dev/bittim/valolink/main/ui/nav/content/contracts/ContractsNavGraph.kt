/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ContractsNavGraph.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.main.ui.nav.content.contracts

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
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
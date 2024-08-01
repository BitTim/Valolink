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
            onNavToContractDetails = { uuid -> navController.navToContractsContractDetails(uuid) }
        )

        contractsGearListScreen(
            onNavBack = { navController.navigateUp() },
            onNavToAgentDetails = { uuid -> navController.navToContractsAgentDetails(uuid) }
        )

        contractsAgentDetailsScreen(
            onNavBack = { navController.navigateUp() },
            onNavToLevelDetails = { uuid, contract ->
                navController.navToContractsLevelDetails(
                    uuid,
                    contract
                )
            }
        )

        contractsContractDetailsScreen(
            onNavBack = { navController.navigateUp() },
            onNavToLevelDetails = { uuid, contract ->
                navController.navToContractsLevelDetails(
                    uuid,
                    contract
                )
            }
        )

        contractsLevelDetailsScreen(
            onNavBack = { navController.navigateUp() }
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
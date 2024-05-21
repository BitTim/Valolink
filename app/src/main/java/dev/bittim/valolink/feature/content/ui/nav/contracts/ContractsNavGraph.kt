package dev.bittim.valolink.feature.content.ui.nav.contracts

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
object ContractsNav

fun NavGraphBuilder.contractsNavGraph(
    navController: NavController
) {
    navigation<ContractsNav>(
        startDestination = ContractsMainNav
    ) {
        contractsMainScreen(
            onNavToGearList = { navController.navToContractsGearList() },
            onNavToAgentDetails = { uuid -> navController.navToContractsAgentDetails(uuid) },
            onNavToContractDetails = { uuid -> navController.navToContractsContractDetails(uuid) }
        )
        contractsGearListScreen(
            onNavBack = { navController.popBackStack() },
            onNavToAgentDetails = { uuid -> navController.navToContractsAgentDetails(uuid) }
        )
        contractsAgentDetailsScreen(
            onNavBack = { navController.popBackStack() })
        contractsContractDetailsScreen(
            onNavBack = { navController.popBackStack() }
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
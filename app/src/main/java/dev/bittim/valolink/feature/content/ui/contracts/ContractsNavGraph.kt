package dev.bittim.valolink.feature.content.ui.contracts

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import dev.bittim.valolink.feature.content.ui.contracts.agentdetails.contractsAgentDetailsScreen
import dev.bittim.valolink.feature.content.ui.contracts.agentdetails.navToContractsAgentDetails
import dev.bittim.valolink.feature.content.ui.contracts.contractdetails.contractsContractDetailsScreen
import dev.bittim.valolink.feature.content.ui.contracts.contractdetails.navToContractsContractDetails
import dev.bittim.valolink.feature.content.ui.contracts.gearlist.contractsGearListScreen
import dev.bittim.valolink.feature.content.ui.contracts.gearlist.navToContractsGearList
import dev.bittim.valolink.feature.content.ui.contracts.main.ContractsMainNavRoute
import dev.bittim.valolink.feature.content.ui.contracts.main.contractsMainScreen

const val ContractsNavRoute = "contracts"
fun NavGraphBuilder.contractsNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = ContractsMainNavRoute, route = ContractsNavRoute
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
    navigate(ContractsNavRoute) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
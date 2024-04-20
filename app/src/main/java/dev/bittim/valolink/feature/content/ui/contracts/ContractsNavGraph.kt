package dev.bittim.valolink.feature.content.ui.contracts

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
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
        contractsMainScreen(onNavToGearList = { navController.navToContractsGearList() })
        contractsGearListScreen(onNavBack = { navController.popBackStack() })
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
package dev.bittim.valolink.feature.content.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import dev.bittim.valolink.feature.content.ui.home.HomeNavRoute
import dev.bittim.valolink.feature.content.ui.home.homeScreen

const val ContentNavRoute = "content"

fun NavGraphBuilder.contentNavGraph(
    navController: NavController,
    onNavAuthGraph: () -> Unit
) {
    navigation(
        startDestination = HomeNavRoute,
        route = ContentNavRoute
    ) {
        homeScreen(onNavAuthGraph)
    }
}

fun NavController.navToContentGraph(origin: String) {
    navigate(ContentNavRoute) {
        popUpTo(origin) {
            inclusive = true
        }
        launchSingleTop = true
    }
}
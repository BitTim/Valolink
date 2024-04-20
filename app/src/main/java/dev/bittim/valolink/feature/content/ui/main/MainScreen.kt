package dev.bittim.valolink.feature.content.ui.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavHostController
import dev.bittim.valolink.feature.content.ui.ContentNavGraph
import dev.bittim.valolink.feature.content.ui.main.components.navbar.NavBar

@Composable
fun MainScreen(
    state: MainState,
    navController: NavHostController,
    onSignOutClicked: () -> Unit,
    onNavAuthGraph: () -> Unit
) {
    if (!state.isAuthenticated) {
        onNavAuthGraph()
    }

    Scaffold(
        Modifier.fillMaxSize(), bottomBar = {
            NavBar(navController = navController)
        }, contentWindowInsets = WindowInsets.systemBars
    ) {
        ContentNavGraph(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = it.calculateLeftPadding(LocalLayoutDirection.current),
                    bottom = it.calculateBottomPadding(),
                    end = it.calculateRightPadding(LocalLayoutDirection.current)
                ), navController = navController, onSignOutClicked = onSignOutClicked
        )
    }
}
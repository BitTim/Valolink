package dev.bittim.valolink.feature.content.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.bittim.valolink.feature.content.ui.ContentNavGraph
import dev.bittim.valolink.feature.content.ui.components.navbar.NavBar

@Composable
fun MainScreen(
    state: MainState,
    navController: NavHostController,
    onCheckAuth: () -> Unit,
    onSignOutClicked: () -> Unit,
    onNavAuthGraph: () -> Unit
) {
    onCheckAuth()

    when (state) {
        is MainState.NoAuth -> {
            LaunchedEffect(key1 = Unit) {
                onNavAuthGraph()
            }
        }

        is MainState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Authenticating...",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.padding(4.dp))
                CircularProgressIndicator()
            }
        }

        is MainState.Content -> {
            Scaffold(
                Modifier.fillMaxSize(),
                bottomBar = { NavBar(navController = navController) }
            ) {
                ContentNavGraph(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    navController = navController,
                    onSignOutClicked = onSignOutClicked
                )
            }
        }
    }
}
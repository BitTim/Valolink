package dev.bittim.valolink.feature.content.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.ui.theme.ValolinkTheme

@Composable
fun HomeScreen(
    state: HomeState,
    onCheckAuth: () -> Unit,
    onSignOutClicked: () -> Unit,
    onNavAuthGraph: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        onCheckAuth()

        when (state) {
            is HomeState.Loading -> {
                CircularProgressIndicator()
            }

            is HomeState.NotAuthorized -> {
                LaunchedEffect(key1 = Unit) {
                    onNavAuthGraph()
                }
            }

            is HomeState.Content -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Welcome back, " + state.username,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    Button(onClick = onSignOutClicked) {
                        Text(text = "Sign out")
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    ValolinkTheme {
        HomeScreen(
            HomeState.Content(
                username = "Test"
            ),
            {}, {}, {}
        )
    }
}
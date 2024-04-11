package dev.bittim.valolink.feature.content.ui.home

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.ui.theme.ValolinkTheme

@Composable
fun HomeScreen(
    state: HomeState,
    onFetch: () -> Unit,
    onSignOutClicked: () -> Unit
) {
    when (state) {
        is HomeState.Fetching -> {
            LaunchedEffect(key1 = Unit) {
                onFetch()
            }
        }

        is HomeState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.padding(4.dp))
                CircularProgressIndicator()
            }
        }

        is HomeState.Content -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Welcome back, ${state.username}",
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ValolinkTheme {
        HomeScreen(
            state = HomeState.Content("John Doe"),
            {}, {}
        )
    }
}
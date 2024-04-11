package dev.bittim.valolink.feature.content.ui.matches

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto
import dev.bittim.valolink.ui.theme.ValolinkTheme

@Composable
fun MatchesScreen(
    state: MatchesState,
    onFetch: () -> Unit
) {
    when (state) {
        is MatchesState.Fetching -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                LaunchedEffect(key1 = Unit) {
                    onFetch()
                }
            }
        }

        is MatchesState.Loading -> {
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

        is MatchesState.Content -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Matches",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.padding(16.dp))

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = state.version?.manifestId ?: "No")
                    Text(text = state.version?.branch ?: "data")
                    Text(text = state.version?.version ?: "has")
                    Text(text = state.version?.buildVersion ?: "been")
                    Text(text = state.version?.engineVersion ?: "loaded")
                    Text(text = state.version?.riotClientVersion ?: "if")
                    Text(text = state.version?.riotClientBuild ?: "you can")
                    Text(text = state.version?.buildDate ?: "read all this")
                }

                if (state.error) {
                    Text(text = "ERROR", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchesScreenPreview() {
    ValolinkTheme {
        MatchesScreen(
            state = MatchesState.Content(
                VersionDto(
                    "a",
                    "b",
                    "c",
                    "d",
                    "e",
                    "f",
                    "g",
                    "h"
                ),
                false
            ),
            onFetch = {}
        )
    }
}
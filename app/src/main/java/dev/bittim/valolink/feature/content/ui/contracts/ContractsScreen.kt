package dev.bittim.valolink.feature.content.ui.contracts

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
import dev.bittim.valolink.ui.theme.ValolinkTheme

@Composable
fun ContractsScreen(
    state: ContractsState,
    onFetch: () -> Unit
) {
    when (state) {
        is ContractsState.Fetching -> {
            LaunchedEffect(key1 = Unit) {
                onFetch()
            }
        }

        is ContractsState.Loading -> {
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

        is ContractsState.Content -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Contracts",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContractsScreenPreview() {
    ValolinkTheme {
        ContractsScreen(
            state = ContractsState.Content,
            onFetch = {}
        )
    }
}
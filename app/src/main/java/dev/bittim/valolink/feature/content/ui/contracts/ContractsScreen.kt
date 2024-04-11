package dev.bittim.valolink.feature.content.ui.contracts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.feature.content.domain.model.Season
import dev.bittim.valolink.feature.content.ui.contracts.components.ContractCard
import dev.bittim.valolink.ui.theme.ValolinkTheme
import java.time.ZonedDateTime

@Composable
fun ContractsScreen(
    state: ContractsState,
    onFetch: () -> Unit
) {
    when (state) {
        is ContractsState.Fetching -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                LaunchedEffect(key1 = Unit) {
                    onFetch()
                }
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
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "Contracts",
                    style = MaterialTheme.typography.headlineMedium
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (state.seasons.isNotEmpty()) {
                        items(
                            items = state.seasons,
                            itemContent = {
                                ContractCard(
                                    name = it.name,
                                    uuid = it.uuid,
                                    startTime = it.startTime,
                                    endTime = it.endTime
                                )
                            }
                        )
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    } else {
                        item(
                            "placeholder",
                        ) {
                            Text(text = "List is empty")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContractsScreenPreview() {
    ValolinkTheme {
        ContractsScreen(
            state = ContractsState.Content(
                listOf(
                    Season(
                        uuid = "b02a8f64-5ba4-45a3-a2e1-24cd0be87627",
                        name = "Test Season",
                        endTime = ZonedDateTime.now().plusDays(10),
                        startTime = ZonedDateTime.now().minusDays(10)
                    )
                )
            ),
            onFetch = {}
        )
    }
}
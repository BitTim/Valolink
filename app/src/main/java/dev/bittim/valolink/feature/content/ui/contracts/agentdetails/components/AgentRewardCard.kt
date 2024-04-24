package dev.bittim.valolink.feature.content.ui.contracts.agentdetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.feature.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.ui.theme.ValolinkTheme

data object AgentRewardCard {
    val width: Dp = 204.dp
}

@Composable
fun AgentRewardCard(
    modifier: Modifier = Modifier,
    name: String,
    type: String,
    price: Int,
    displayIcon: String,
    currencyIcon: String,
) {
    Card(
        modifier = modifier
            .width(AgentRewardCard.width)
            .aspectRatio(1f)
            .fillMaxSize()
    ) {
        Box {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = displayIcon,
                contentDescription = null,
                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_displayicon)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            Pair(0f, Color.Transparent),
                            Pair(0.7f, CardDefaults.cardColors().containerColor)
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = type,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = { }) {
                            AsyncImage(
                                modifier = Modifier
                                    .width(16.dp)
                                    .aspectRatio(1f),
                                model = currencyIcon,
                                contentDescription = null,
                                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_kingdom_kreds)
                            )

                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = "$price"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgentRewardCardPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.height(204.dp)
        ) {
            AgentRewardCard(
                name = "Gloomheart",
                type = "Spray",
                price = 2000,
                displayIcon = "https://media.valorant-api.com/sprays/7221ab04-4a64-9a0f-ba1e-e7a423f5ed4b/displayicon.png",
                currencyIcon = "https://media.valorant-api.com/currencies/85ca954a-41f2-ce94-9b45-8ca3dd39a00d/displayicon.png"
            )
        }
    }
}
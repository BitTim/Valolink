package dev.bittim.valolink.feature.content.ui.screens.contracts.agentdetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.feature.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.ui.theme.ValolinkTheme

@Composable
fun AbilityDetailsItem(
    modifier: Modifier = Modifier,
    displayName: String,
    slot: String,
    description: String,
    displayIcon: String?,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(48.dp)
                    .aspectRatio(1f),
                model = displayIcon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_ability_icon)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = displayName, style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = slot,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = description, style = MaterialTheme.typography.bodyMedium
        )
    }
}



@Preview(showBackground = true)
@Composable
fun AbilityDetailsItemPreview() {
    ValolinkTheme {
        Column(
            modifier = Modifier.wrapContentHeight()
        ) {
            AbilityDetailsItem(
                displayName = "Pick-Me-Up",
                slot = "Grenade",
                description = "ACTIVATE to absorb the life force of a fallen enemy that Clove damaged or killed, gaining haste and temporary health.",
                displayIcon = "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/abilities/grenade/displayicon.png"
            )
        }
    }
}
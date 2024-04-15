package dev.bittim.valolink.feature.content.ui.contracts.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ContractCard(
    modifier: Modifier = Modifier,
    name: String,
    uuid: String,
    relationType: String?,
    relationUuid: String?
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                text = uuid,
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = relationType ?: "null",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = relationUuid ?: "null",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
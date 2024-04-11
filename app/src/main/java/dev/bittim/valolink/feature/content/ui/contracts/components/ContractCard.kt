package dev.bittim.valolink.feature.content.ui.contracts.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ContractCard(
    modifier: Modifier = Modifier,
    name: String,
    uuid: String,
    startTime: ZonedDateTime,
    endTime: ZonedDateTime
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

            Spacer(modifier = Modifier.padding(4.dp))

            Row(
                Modifier.fillMaxWidth()
            ) {
                Text(
                    text = startTime.format(
                        DateTimeFormatter.ofLocalizedDateTime(
                            FormatStyle.SHORT
                        )
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = " • ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = endTime.format(
                        DateTimeFormatter.ofLocalizedDateTime(
                            FormatStyle.SHORT
                        )
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
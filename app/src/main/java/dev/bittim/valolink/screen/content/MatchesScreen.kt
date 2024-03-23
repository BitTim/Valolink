package dev.bittim.valolink.screen.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MatchesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Matches",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "This is a placeholder since the actual content is not available yet.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
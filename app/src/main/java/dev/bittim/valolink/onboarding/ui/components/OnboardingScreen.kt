package dev.bittim.valolink.onboarding.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.ui.theme.ValolinkTheme

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    title: String,
    progress: Float,
    description: String,
    content: @Composable () -> Unit,
    buttons: @Composable () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OnboardingHeader(
            modifier = Modifier.fillMaxWidth(),
            title = title,
            progress = progress
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(16.dp)
        ) {
            content()
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            buttons()
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OnboardingScreenPreview() {
    ValolinkTheme {
        Surface {
            OnboardingScreen(
                modifier = Modifier.fillMaxSize(),
                title = "Sample section",
                progress = 0.37f,
                description = "This is a sample section that is supposed to serve as a preview to this composable. If this text is visible, then this sample is working fine. If you see this in the finished app, something went wrong... Like really wrong",
                content = {
                    Box(
                        Modifier
                            .background(Color.Red)
                            .fillMaxSize()
                    )
                },
                buttons = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(onClick = {}) { Text("Back") }
                        Button(onClick = {}) { Text("Continue") }
                    }
                }
            )
        }
    }
}
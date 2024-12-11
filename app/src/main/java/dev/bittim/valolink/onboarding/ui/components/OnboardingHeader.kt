package dev.bittim.valolink.onboarding.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.annotations.ComponentPreviewAnnotations

@Composable
fun OnboardingHeader(
    modifier: Modifier = Modifier,
    title: String,
    progress: Float,
    description: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.l)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall
            )

            CircularProgressIndicator(progress = { progress })
        }

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@ComponentPreviewAnnotations
@Composable
fun OnboardingHeaderPreview() {
    ValolinkTheme {
        Surface {
            OnboardingHeader(
                modifier = Modifier.fillMaxWidth(),
                title = "Sample section",
                progress = 0.37f,
                description = "This is a sample section that is supposed to serve as a preview to this composable. If this text is visible, then this sample is working fine. If you see this in the finished app, something went wrong... Like really wrong"
            )
        }
    }
}
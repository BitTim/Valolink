package dev.bittim.valolink.onboarding.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.theme.ValolinkTheme

@Composable
fun OnboardingHeader(
    modifier: Modifier = Modifier,
    title: String,
    progress: Float,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall
        )

        CircularProgressIndicator(progress = { progress })
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OnboardingHeaderPreview() {
    ValolinkTheme {
        Surface {
            OnboardingHeader(
                modifier = Modifier.fillMaxWidth(),
                title = "Sample section",
                progress = 0.37f,
            )
        }
    }
}
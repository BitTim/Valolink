package dev.bittim.valolink.main.ui.screens.onboarding.getstarted

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.ui.theme.ValolinkTheme

@Composable
fun GetStartedScreen(
    state: GetStartedState,
    onGetStartedClicked: () -> Unit,
) {
    val isLoading: Boolean = state.loadingFinished != 0b0011

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Valolink",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                enabled = !isLoading,
                onClick = onGetStartedClicked
            ) {
                Text(text = if (isLoading) "Loading..." else "Get started")
            }
        }
    }
}

@Preview
@Composable
fun GetStartedScreenLoadingPreview() {
    ValolinkTheme {
        GetStartedScreen(state = GetStartedState(0b0011),
                         onGetStartedClicked = {})
    }
}

@Preview
@Composable
fun GetStartedScreenPreview() {
    ValolinkTheme {
        GetStartedScreen(state = GetStartedState(0b0000),
                         onGetStartedClicked = {})
    }
}
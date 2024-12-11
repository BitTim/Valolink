package dev.bittim.valolink.onboarding.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.util.UiText

@Composable
fun OnboardingButtons(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onContinue: () -> Unit,
    dismissText: UiText = UiText.StringResource(R.string.button_back),
    continueText: UiText = UiText.StringResource(R.string.button_continue)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(onClick = onDismiss) {
            Text(text = dismissText.asString())
        }

        Button(onClick = onContinue) {
            Text(text = continueText.asString())
        }
    }
}
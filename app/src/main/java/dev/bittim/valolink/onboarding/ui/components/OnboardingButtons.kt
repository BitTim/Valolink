/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       OnboardingButtons.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 14:44
 */

package dev.bittim.valolink.onboarding.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
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
    disableContinueButton: Boolean,
    dismissText: UiText = UiText.StringResource(R.string.button_back),
    continueText: UiText = UiText.StringResource(R.string.button_continue)
) {
    Row(
        modifier = modifier.wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(onClick = onDismiss) {
            Text(text = dismissText.asString())
        }

        Button(onClick = onContinue, enabled = !disableContinueButton) {
            Text(text = continueText.asString())
        }
    }
}

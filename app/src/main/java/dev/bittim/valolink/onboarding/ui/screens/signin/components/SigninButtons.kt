/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       SigninButtons.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 19:39
 */

package dev.bittim.valolink.onboarding.ui.screens.signin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.util.UiText

@Composable
fun SigninButtons(
    modifier: Modifier = Modifier,
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = onForgotPassword) {
            Text(
                text = UiText.StringResource(R.string.onboarding_signin_button_forgotPassword)
                    .asString()
            )
        }

        TextButton(onClick = onCreateAccount) {
            Text(
                text = UiText.StringResource(R.string.onboarding_signin_button_createAccount)
                    .asString()
            )
        }
    }
}

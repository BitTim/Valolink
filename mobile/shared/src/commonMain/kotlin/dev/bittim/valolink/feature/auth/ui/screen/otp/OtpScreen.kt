/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       OtpScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 02:30
 */

package dev.bittim.valolink.feature.auth.ui.screen.otp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*

@Composable
@Preview
fun OtpScreen(
    state: OtpScreenState = OtpScreenState(),
    onOtpChange: (otp: String) -> Unit = {},
    onComplete: () -> Unit = {},
    navBack: () -> Unit = {}
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(Spacing.l),
            verticalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .weight(1f)
            ) {}

            Column(
                modifier = Modifier.fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                // TODO change with actual string resources
                Text(
                    text = stringResource(resource = Res.string.auth_password_title_login),
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = stringResource(resource = Res.string.auth_password_description_login),
                    style = MaterialTheme.typography.bodyMedium
                )

                // TODO change when actual composable is present
                OutlinedTextField(
                    value = state.otp,
                    onValueChange = {
                        onOtpChange(if (it.length > 8) it.take(8) else it)
                    },
                    label = { Text("OTP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { navBack() }
                    ) {
                        Text(
                            stringResource(resource = Res.string.generic_button_back)
                        )
                    }

                    Button(
                        enabled = state.error == null && state.otp.isNotEmpty(),
                        onClick = { onComplete() }
                    ) {
                        Text(
                            stringResource(resource = Res.string.generic_button_finish)
                        )
                    }
                }
            }
        }
    }
}
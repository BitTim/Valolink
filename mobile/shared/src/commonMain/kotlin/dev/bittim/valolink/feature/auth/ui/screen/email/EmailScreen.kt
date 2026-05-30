/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       EmailScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 02:30
 */

package dev.bittim.valolink.feature.auth.ui.screen.email

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*

@Composable
@Preview
fun EmailScreen(
    state: EmailScreenState = EmailScreenState(),
    onEmailChange: (email: String) -> Unit = {},
    onNext: () -> Unit = {},
    navBack: () -> Unit = {}
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(Spacing.l),
            verticalArrangement = Arrangement.spacedBy(Spacing.m),
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
                Text(
                    text = stringResource(resource = Res.string.auth_email_title),
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = stringResource(resource = Res.string.auth_email_description),
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(resource = Res.string.auth_email_textField_label),
                    value = state.email,
                    error = state.error?.let { stringResource(it) },
                    onValueChange = { onEmailChange(it) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = stringResource(Res.string.iconcd_email))},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                        enabled = state.error == null && state.email.isNotEmpty(),
                        onClick = { onNext() }
                    ) {
                        Text(
                            stringResource(resource = Res.string.generic_button_continue)
                        )
                    }
                }
            }
        }
    }
}
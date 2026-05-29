/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       EmailScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.05.26, 15:52
 */

package dev.bittim.valolink.feature.auth.ui.screen.email

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    validateEmail: (email: String) -> Unit = {},
    onNext: (email: String) -> Unit = {},
    navBack: () -> Unit = {}
) {
    var email: String by remember { mutableStateOf("") }

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
                    value = email,
                    error = state.error?.let { stringResource(it) },
                    onValueChange = {
                        email = it
                        validateEmail(it)
                    },
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
                        enabled = state.error == null && email.isNotEmpty(),
                        onClick = { onNext(email) }
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
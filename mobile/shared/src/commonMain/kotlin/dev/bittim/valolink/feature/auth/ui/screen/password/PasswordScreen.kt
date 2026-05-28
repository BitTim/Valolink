/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       PasswordScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   28.05.26, 21:17
 */

package dev.bittim.valolink.feature.auth.ui.screen.password

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
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
fun PasswordScreen(
    state: PasswordScreenState = PasswordScreenState(),
    validatePassword: (password: String) -> Unit = {},
    navBack: () -> Unit = {},
    navNext: () -> Unit = {},
) {
    var password: String by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

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
                    text = stringResource(resource = Res.string.auth_password_title_login),
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = stringResource(resource = Res.string.auth_password_description_login),
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(resource = Res.string.auth_password_textField_label),
                    value = password,
                    error = state.error?.let { stringResource(it) },
                    onValueChange = {
                        password = it
                        validatePassword(it)
                    },
                    leadingIcon = { Icon(Icons.Default.Password, contentDescription = stringResource(Res.string.iconcd_password))},
                    enableVisibilityToggle = true,
                    visibility = passwordVisibility,
                    onVisibilityChange = { passwordVisibility = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(onNext = { navNext() })
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
                        enabled = state.error == null && password.isNotEmpty(),
                        onClick = { navNext() }
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
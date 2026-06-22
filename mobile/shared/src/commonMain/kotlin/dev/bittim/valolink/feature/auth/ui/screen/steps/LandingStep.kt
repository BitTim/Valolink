/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       LandingStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.05.26, 21:37
 */

package dev.bittim.valolink.feature.auth.ui.screen.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.feature.auth.ui.components.OutlinedSocialButton
import dev.bittim.valolink.feature.auth.ui.screen.AuthFlowAction
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*

@Composable
@Preview
fun LandingStep(
    modifier: Modifier = Modifier,
    email: String = "",
    error: StringResource? = null,
    onAction: (action: AuthFlowAction) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.s)
        ) {
            Text(
                text = stringResource(resource = Res.string.auth_landing_title),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = stringResource(resource = Res.string.auth_landing_description),
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextFieldWithError(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(resource = Res.string.auth_landing_text_field_label),
                value = email,
                error = error?.let { stringResource(it) },
                onValueChange = { onAction(AuthFlowAction.EmailChange(it)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = stringResource(Res.string.iconcd_email)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotEmpty() && error == null,
                onClick = { onAction(AuthFlowAction.EmailContinue) },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s)
                ) {
                    Icon(
                        Icons.Outlined.Email,
                        contentDescription = stringResource(Res.string.iconcd_email),
                    )

                    Text(text = stringResource(Res.string.auth_landing_button_email))
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.m))

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            OutlinedSocialButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = false, // TODO: Implement Google sign-in
                onClick = {},
                painter = painterResource(Res.drawable.ic_google_logo),
                contentDescription = stringResource(Res.string.iconcd_google),
                text = stringResource(Res.string.auth_landing_button_google)
            )

            OutlinedSocialButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = false, // TODO: Implement Apple sign-in
                onClick = {},
                painter = painterResource(Res.drawable.ic_apple_logo),
                contentDescription = stringResource(Res.string.iconcd_apple),
                text = stringResource(Res.string.auth_landing_button_apple)
            )
        }
    }
}
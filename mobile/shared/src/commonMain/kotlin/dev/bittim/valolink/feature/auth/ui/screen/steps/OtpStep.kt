/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       OtpStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.06.26, 19:29
 */

package dev.bittim.valolink.feature.auth.ui.screen.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.feature.auth.ui.screen.AuthFlowAction
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
@Preview
fun OtpStep(
    modifier: Modifier = Modifier,
    email: String = "",
    otp: String = "",
    error: StringResource? = null,
    cooldownSecondsLeft: Int? = 0,
    onAction: (AuthFlowAction) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.s)
    ) {
        Text(
            text = stringResource(resource = Res.string.auth_otp_title),
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = stringResource(resource = Res.string.auth_otp_description),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = email,
            style = MaterialTheme.typography.labelLarge
        )

        // TODO: Replace with a segmented umber input field
        OutlinedTextFieldWithError(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(resource = Res.string.auth_otp_label),
            value = otp,
            error = error?.let { stringResource(it) },
            onValueChange = {
                onAction(AuthFlowAction.OtpChange(it))
            },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Key, contentDescription = stringResource(Res.string.iconcd_key)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        )

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = cooldownSecondsLeft == 0,
            onClick = { onAction(AuthFlowAction.OtpResend) }
        ) {
            Row {
                Text(
                    stringResource(resource = Res.string.auth_otp_resend)
                )

                AnimatedVisibility(cooldownSecondsLeft?.let { it > 0 } ?: false) {
                    Text(
                        " " + stringResource(Res.string.auth_otp_cooldown, cooldownSecondsLeft ?: 0)
                    )
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = error == null && otp.isNotEmpty(),
            onClick = { onAction(AuthFlowAction.OtpContinue) }
        ) {
            Text(
                stringResource(resource = Res.string.generic_button_finish)
            )
        }
    }
}
/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       OtpStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.05.26, 21:42
 */

package dev.bittim.valolink.feature.auth.ui.screen.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@Composable
@Preview
fun OtpStep(
    modifier: Modifier = Modifier,
    otp: String = "",
    error: StringResource? = null,
    onAction: (action: AuthFlowAction) -> Unit = {}
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
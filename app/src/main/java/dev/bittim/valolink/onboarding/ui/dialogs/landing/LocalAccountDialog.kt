/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LocalAccountDialog.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.04.25, 15:50
 */

package dev.bittim.valolink.onboarding.ui.dialogs.landing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PublicOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations

@Composable
fun LocalAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.PublicOff,
                contentDescription = null
            )
        },
        title = {
            Text(
                UiText.StringResource(R.string.onboarding_dialog_localAccount_title).asString()
            )
        },
        text = {
            Text(
                UiText.StringResource(R.string.onboarding_dialog_localAccount_content).asString()
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(
                    UiText.StringResource(R.string.onboarding_dialog_localAccount_button_confirm)
                        .asString()
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    UiText.StringResource(R.string.button_cancel).asString()
                )
            }
        }
    )
}

@ScreenPreviewAnnotations
@Composable
fun LocalAccountDialogPreview() {
    ValolinkTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            LocalAccountDialog(
                onConfirm = {},
                onDismiss = {}
            )
        }
    }
}

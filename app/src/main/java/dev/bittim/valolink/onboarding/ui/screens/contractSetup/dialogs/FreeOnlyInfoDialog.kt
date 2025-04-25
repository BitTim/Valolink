/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       FreeOnlyInfoDialog.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   25.04.25, 04:25
 */

package dev.bittim.valolink.onboarding.ui.screens.contractSetup.dialogs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
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
fun FreeOnlyInfoDialog(
    onDismiss: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.HelpOutline,
                contentDescription = null
            )
        },
        title = {
            Text(
                UiText.StringResource(R.string.onboarding_dialog_freeOnly_title).asString()
            )
        },
        text = {
            Text(
                UiText.StringResource(R.string.onboarding_dialog_freeOnly_content).asString()
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = UiText.StringResource(R.string.button_ok).asString())
            }
        }
    )
}

@ScreenPreviewAnnotations
@Composable
fun FreeOnlyInfoDialogPreview() {
    ValolinkTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            FreeOnlyInfoDialog(
                onDismiss = {}
            )
        }
    }
}

/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractResetAlertDialog.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:30
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ContractResetAlertDialog(
    contractName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.Restore,
                contentDescription = null
            )
        },
        title = { Text(text = "Reset $contractName") },
        text = { Text(text = "Your level progress for $contractName will be reset. This does not effect your actual progress in game.") },
        onDismissRequest = onDismiss,

        confirmButton = {
            Button(onClick = {
                onDismiss()
                onConfirm()
            }) {
                Text(text = "Reset")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}

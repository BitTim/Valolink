package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AgentResetAlertDialog(
    agentName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(icon = {
        Icon(
            imageVector = Icons.Default.Restore,
            contentDescription = null
        )
    },
                title = { Text(text = "Reset $agentName") },
                text = { Text(text = "Your reward progress for $agentName will be reset, and $agentName will be locked again. This does not effect your actual progress in game.") },
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
                })
}
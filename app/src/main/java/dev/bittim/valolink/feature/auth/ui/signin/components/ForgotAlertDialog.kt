package dev.bittim.valolink.feature.auth.ui.signin.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.feature.auth.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.ui.theme.ValolinkTheme

@Composable
fun ForgotAlertDialog(
    email: String,
    emailError: String?,
    onEmailValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        title = { Text(text = "Request password reset") },
        text = {
            Column {
                Text(text = "Enter your email address to receive a password reset link, if the email is registered.")
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextFieldWithError(
                    label = "Email",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email"
                        )
                    },
                    value = email,
                    error = emailError,
                    enableVisibilityToggle = false,
                    onValueChange = onEmailValueChange,
                )
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(text = "Send request")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ForgotAlertDialogPreview() {
    ValolinkTheme {
        ForgotAlertDialog(
            email = "",
            emailError = null,
            {}, {}, {}
        )
    }
}
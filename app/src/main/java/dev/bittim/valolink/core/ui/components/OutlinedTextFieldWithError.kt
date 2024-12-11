package dev.bittim.valolink.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.annotations.ComponentPreviewAnnotations

@Composable
fun OutlinedTextFieldWithError(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    enableVisibilityToggle: Boolean = false,
    visibility: Boolean = !enableVisibilityToggle,
    onVisibilityChange: (Boolean) -> Unit = {},
) {
    var visibilityState by remember { mutableStateOf(visibility) }

    Column {
        OutlinedTextField(
            modifier = modifier,
            value = value,
            isError = error != null,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            leadingIcon = leadingIcon,
            singleLine = true,
            visualTransformation = if (visibilityState) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                if (enableVisibilityToggle) {
                    IconToggleButton(
                        checked = visibilityState,
                        onCheckedChange = {
                            visibilityState = !visibilityState
                            onVisibilityChange(it)
                        },
                    ) {
                        if (visibilityState) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "Hide contents"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "Show contents"
                            )
                        }
                    }
                }
            }
        )

        if (!error.isNullOrEmpty()) {
            Text(
                modifier = Modifier.padding(top = Spacing.s),
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}


@ComponentPreviewAnnotations
@Composable
fun OutlinedTextFieldWithErrorPreview() {
    ValolinkTheme {
        Surface {
            var value: String by remember { mutableStateOf("") }
            var error: String? by remember { mutableStateOf(null) }

            OutlinedTextFieldWithError(
                modifier = Modifier.fillMaxWidth(),
                label = "Username",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null
                    )
                },
                value = value,
                error = error,
                enableVisibilityToggle = true,
                onValueChange = {
                    value = it

                    error = if (it.lowercase() == "error") {
                        "This is a sample error"
                    } else {
                        null
                    }
                }
            )
        }
    }
}
package dev.bittim.valolink.auth.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.ui.theme.ValolinkTheme

@Composable
fun OutlinedTextFieldWithError(
    label: String,
    leadingIcon: @Composable (() -> Unit)?,
    value: String,
    error: String?,
    enableVisibilityToggle: Boolean,
    onValueChange: (String) -> Unit,
) {
    var visibility by remember { mutableStateOf(!enableVisibilityToggle) }
    OutlinedTextFieldWithError(
        visibility = visibility,
        label = label,
        leadingIcon = leadingIcon,
        value = value,
        error = error,
        enableVisibilityToggle = enableVisibilityToggle,
        onVisibilityChange = { visibility = it },
        onValueChange = onValueChange
    )
}

@Composable
fun OutlinedTextFieldWithError(
    visibility: Boolean,
    label: String,
    leadingIcon: @Composable (() -> Unit)?,
    value: String,
    error: String?,
    enableVisibilityToggle: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit,
) {
    Column {
        OutlinedTextField(value = value,
                          isError = error != null,
                          onValueChange = onValueChange,
                          modifier = Modifier.fillMaxWidth(),
                          label = { Text(text = label) },
                          leadingIcon = leadingIcon,
                          singleLine = true,
                          visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                          trailingIcon = {
                              if (enableVisibilityToggle) {
                                  IconToggleButton(
                                      checked = visibility,
                                      onCheckedChange = onVisibilityChange,
                                  ) {
                                      if (visibility) {
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
                          })

        if (!error.isNullOrEmpty()) {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyOutlinedTextFieldWithErrorPreview() {
    ValolinkTheme {
        OutlinedTextFieldWithError(visibility = true,
                                   label = "Username",
                                   leadingIcon = {
                                       Icon(
                                           imageVector = Icons.Filled.Person,
                                           contentDescription = "Username"
                                       )
                                   },
                                   value = "",
                                   error = null,
                                   enableVisibilityToggle = false,
                                   {},
                                   {})
    }
}

@Preview(showBackground = true)
@Composable
fun HiddenEmptyOutlinedTextFieldWithErrorPreview() {
    ValolinkTheme {
        OutlinedTextFieldWithError(visibility = false,
                                   label = "Password",
                                   leadingIcon = {
                                       Icon(
                                           imageVector = Icons.Filled.Password,
                                           contentDescription = "Password"
                                       )
                                   },
                                   value = "",
                                   error = null,
                                   enableVisibilityToggle = true,
                                   {},
                                   {})
    }
}

@Preview(showBackground = true)
@Composable
fun HiddenFilledOutlinedTextFieldWithErrorPreview() {
    ValolinkTheme {
        OutlinedTextFieldWithError(visibility = false,
                                   label = "Password",
                                   leadingIcon = {
                                       Icon(
                                           imageVector = Icons.Filled.Password,
                                           contentDescription = "Password"
                                       )
                                   },
                                   value = "Test1234",
                                   error = null,
                                   enableVisibilityToggle = true,
                                   {},
                                   {})
    }
}

@Preview(showBackground = true)
@Composable
fun VisibleFilledOutlinedTextFieldWithErrorPreview() {
    ValolinkTheme {
        OutlinedTextFieldWithError(visibility = true,
                                   label = "Password",
                                   leadingIcon = {
                                       Icon(
                                           imageVector = Icons.Filled.Password,
                                           contentDescription = "Password"
                                       )
                                   },
                                   value = "Test1234",
                                   error = null,
                                   enableVisibilityToggle = true,
                                   {},
                                   {})
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorEmptyOutlinedTextFieldWithErrorPreview() {
    ValolinkTheme {
        OutlinedTextFieldWithError(visibility = true,
                                   label = "Username",
                                   leadingIcon = {
                                       Icon(
                                           imageVector = Icons.Filled.Person,
                                           contentDescription = "Username"
                                       )
                                   },
                                   value = "",
                                   error = "Username cannot be empty",
                                   enableVisibilityToggle = false,
                                   {},
                                   {})
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorHiddenEmptyOutlinedTextFieldWithErrorPreview() {
    ValolinkTheme {
        OutlinedTextFieldWithError(visibility = false,
                                   label = "Password",
                                   leadingIcon = {
                                       Icon(
                                           imageVector = Icons.Filled.Password,
                                           contentDescription = "Password"
                                       )
                                   },
                                   value = "",
                                   error = "Password cannot be empty",
                                   enableVisibilityToggle = true,
                                   {},
                                   {})
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorHiddenFilledOutlinedTextFieldWithErrorPreview() {
    ValolinkTheme {
        OutlinedTextFieldWithError(visibility = false,
                                   label = "Password",
                                   leadingIcon = {
                                       Icon(
                                           imageVector = Icons.Filled.Password,
                                           contentDescription = "Password"
                                       )
                                   },
                                   value = "Test1234",
                                   error = "Passwords do not match",
                                   enableVisibilityToggle = true,
                                   {},
                                   {})
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorVisibleFilledOutlinedTextFieldWithErrorPreview() {
    ValolinkTheme {
        OutlinedTextFieldWithError(visibility = true,
                                   label = "Password",
                                   leadingIcon = {
                                       Icon(
                                           imageVector = Icons.Filled.Password,
                                           contentDescription = "Username"
                                       )
                                   },
                                   value = "Test1234",
                                   error = "Passwords do not match",
                                   enableVisibilityToggle = true,
                                   {},
                                   {})
    }
}
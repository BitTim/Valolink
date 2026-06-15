/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       OutlinedTextFieldWithError.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 14:23
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import dev.bittim.valolink.core.ui.Spacing

@Composable
fun OutlinedTextFieldWithError(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    enableVisibilityToggle: Boolean = false,
    visibility: Boolean = !enableVisibilityToggle,
    onVisibilityChange: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var visibilityState by remember { mutableStateOf(visibility) }

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            isError = error != null,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            leadingIcon = leadingIcon,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
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

        AnimatedContent(
            targetState = error,
            label = "Animate error visibility"
        ) {
            if (it != null) {
                Text(
                    modifier = Modifier.padding(top = Spacing.s),
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
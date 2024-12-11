/*
Copyright (c) 2024 BitTim

Project:        Valolink
License:        GPLv3

File:           AutofillExtensions.kt
Author:         Bagadesh (https://medium.com/@bagadeshrp/compose-ui-textfield-autofill-6e2ac434e380)
Created:        11.12.2024
Description:    Extension functions for initializing autofill for composable
*/

package dev.bittim.valolink.core.ui.util.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import dev.bittim.valolink.core.ui.util.autofill.AutofillHandler

fun Modifier.connectNode(handler: AutofillHandler): Modifier {
    return with(handler) { fillBounds() }
}

fun Modifier.defaultFocusChangeAutoFill(handler: AutofillHandler): Modifier {
    return this.then(
        Modifier.onFocusChanged {
            if (it.isFocused) {
                handler.request()
            } else {
                handler.cancel()
            }
        }
    )
}
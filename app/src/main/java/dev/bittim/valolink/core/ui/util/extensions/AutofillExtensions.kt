/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       AutofillExtensions.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
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
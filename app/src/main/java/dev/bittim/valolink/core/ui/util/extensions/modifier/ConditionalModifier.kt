/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ConditionalModifier.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.04.25, 16:09
 */

package dev.bittim.valolink.core.ui.util.extensions.modifier

import androidx.compose.ui.Modifier

fun Modifier.conditional(
    condition: Boolean,
    modifier: Modifier.() -> Modifier,
): Modifier {
    return if (condition) {
        then(modifier(Modifier.Companion))
    } else {
        this
    }
}

/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UnlockButton.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.core.ui.theme.Spacing

@Composable
fun UnlockButton(
    modifier: Modifier = Modifier,
    currencyIcon: String,
    price: Int,
    isPrimary: Boolean,
    isLocked: Boolean,
    isOwned: Boolean,
    onClick: () -> Unit,
) {
    @Composable
    fun ButtonContent(iconColor: Color) {
        if (!isOwned && !isLocked) {
            AsyncImage(
                modifier = Modifier
                    .height(Spacing.xl)
                    .aspectRatio(1f),
                model = currencyIcon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconColor),
                placeholder = coilDebugPlaceholder(
                    debugPreview = R.drawable.debug_kingdom_kreds
                )
            )

            Spacer(modifier = Modifier.width(Spacing.s))
            Text(text = "$price")
        } else {
            if (isLocked) Text(text = "Locked")
            else Text(text = "Owned")
        }
    }

    if (!isPrimary) {
        FilledTonalButton(
            modifier = modifier,
            enabled = !isOwned && !isLocked,
            onClick = onClick
        ) {
            ButtonContent(MaterialTheme.colorScheme.onSecondaryContainer)
        }
    } else {
        Button(
            modifier = modifier,
            enabled = !isOwned && !isLocked,
            onClick = onClick
        ) {
            ButtonContent(MaterialTheme.colorScheme.onPrimary)
        }
    }
}

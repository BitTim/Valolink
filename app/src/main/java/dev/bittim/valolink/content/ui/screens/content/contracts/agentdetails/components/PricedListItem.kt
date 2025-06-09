/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       PricedListItem.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
fun PricedListItem(
    itemName: String,
    currencyIcon: String?,
    price: Int,
    tint: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = itemName)
        Row {
            Text(text = "$price")
            Spacer(modifier = Modifier.width(Spacing.xs))

            AsyncImage(
                modifier = Modifier
                    .padding(end = Spacing.s)
                    .width(Spacing.l)
                    .aspectRatio(1f),
                model = currencyIcon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(tint),
                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_kingdom_kreds)
            )
        }
    }
}

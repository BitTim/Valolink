/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       PricedListItem.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:30
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder

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
            Spacer(modifier = Modifier.width(4.dp))

            AsyncImage(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(16.dp)
                    .aspectRatio(1f),
                model = currencyIcon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(tint),
                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_kingdom_kreds)
            )
        }
    }
}

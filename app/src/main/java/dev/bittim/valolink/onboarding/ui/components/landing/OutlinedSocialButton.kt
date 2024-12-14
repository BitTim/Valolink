/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       OutlinedSocialButton.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:30
 */

package dev.bittim.valolink.onboarding.ui.components.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data object OutlinedSocialButton {
    val ICON_SIZE: Dp = 20.dp
    val ICON_PADDING: Dp = 10.dp
}

@Composable
fun OutlinedSocialButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    painter: Painter,
    contentDescription: String?,
    text: String,
    enabled: Boolean = true
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled
    ) {
        Image(
            modifier = Modifier
                .height(OutlinedSocialButton.ICON_SIZE)
                .aspectRatio(1f),
            painter = painter,
            contentDescription = contentDescription
        )
        Spacer(Modifier.width(OutlinedSocialButton.ICON_PADDING))
        Text(text = text)
    }
}
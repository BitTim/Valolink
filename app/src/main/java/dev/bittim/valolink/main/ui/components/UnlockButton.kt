package dev.bittim.valolink.main.ui.components

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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R

@Composable
fun UnlockButton(
    modifier: Modifier = Modifier,
    currencyIcon: String,
    price: Int,
    isPrimary: Boolean,
    onClick: () -> Unit = {}
) {
    @Composable
    fun ButtonContent(iconColor: Color) {
        AsyncImage(
            modifier = Modifier
                .height(24.dp)
                .aspectRatio(1f),
            model = currencyIcon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(iconColor),
            placeholder = coilDebugPlaceholder(
                debugPreview = R.drawable.debug_kingdom_kreds
            )
        )

        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$price")
    }

    if (!isPrimary) {
        FilledTonalButton(
            modifier = modifier,
            onClick = onClick
        ) {
            ButtonContent(MaterialTheme.colorScheme.onSecondaryContainer)
        }
    } else {
        Button(
            modifier = modifier,
            onClick = onClick
        ) {
            ButtonContent(MaterialTheme.colorScheme.onPrimary)
        }
    }
}
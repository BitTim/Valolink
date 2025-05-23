/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ChromaToggleButton.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:37
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.core.ui.theme.ValolinkTheme

@Composable
fun ChromaToggleButton(
    modifier: Modifier = Modifier,
    swatch: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    FilledIconToggleButton(
        modifier = modifier
            .padding(4.dp)
            .height(52.dp)
            .aspectRatio(1f),
        checked = checked,
        onCheckedChange = onCheckedChange
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .clip(CircleShape),
            model = swatch,
            contentDescription = null,
            placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_skin_chroma_swatch)
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ChromaToggleButtonPreview() {
    ValolinkTheme {
        Surface {
            ChromaToggleButton(
                swatch = "https://media.valorant-api.com/weaponskinchromas/2984c2a8-40e1-69eb-36b2-bea3c6f9b82d/swatch.png",
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun CheckedChromaToggleButtonPreview() {
    ValolinkTheme {
        Surface {
            ChromaToggleButton(
                swatch = "https://media.valorant-api.com/weaponskinchromas/2984c2a8-40e1-69eb-36b2-bea3c6f9b82d/swatch.png",
                checked = true,
                onCheckedChange = {}
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun IntegratedChromaToggleButtonPreview() {
    ValolinkTheme {
        Surface {
            Row {
                ChromaToggleButton(
                    swatch = "https://media.valorant-api.com/weaponskinchromas/2984c2a8-40e1-69eb-36b2-bea3c6f9b82d/swatch.png",
                    checked = true,
                    onCheckedChange = {}
                )

                ChromaToggleButton(
                    swatch = "https://media.valorant-api.com/weaponskinchromas/2984c2a8-40e1-69eb-36b2-bea3c6f9b82d/swatch.png",
                    checked = false,
                    onCheckedChange = {}
                )

                FilledIconToggleButton(checked = true, onCheckedChange = {}) {
                    Icon(imageVector = Icons.Filled.Android, contentDescription = null)
                }

                FilledIconToggleButton(checked = false, onCheckedChange = {}) {
                    Icon(imageVector = Icons.Filled.Android, contentDescription = null)
                }
            }
        }
    }
}

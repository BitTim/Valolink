/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       VariantPreviewCluster.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 17:49
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop169
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.outlined.Crop169
import androidx.compose.material.icons.outlined.CropPortrait
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.components.ConnectedButtonEntry
import dev.bittim.valolink.core.ui.components.SingleConnectedButtonGroup
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.ToggleIcon
import dev.bittim.valolink.core.ui.util.UiText

@Composable
fun VariantPreviewCluster(
    modifier: Modifier = Modifier,
    variants: List<Any?> = emptyList(),
    selected: Int = 0,
    onSelected: (Int) -> Unit,
) {
    var selected by remember {
        mutableIntStateOf(selected)
    }

    Surface(
        modifier = modifier.padding(8.dp),
        shape = MaterialTheme.shapes.extraLarge,
        shadowElevation = 3.dp,
        color = MaterialTheme.colorScheme.surface,
    ) {
        SingleConnectedButtonGroup(
            modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            entries = variants.map {
                ConnectedButtonEntry(
                    icon = it as? ToggleIcon,
                    image = it as? String,
                )
            },
            fillWidth = false,
            onSelectedChanged = {
                selected = it
                onSelected(it)
            }
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewClusterPlayerCard() {
    ValolinkTheme {
        Surface {
            VariantPreviewCluster(
                variants = listOf(
                    ToggleIcon(
                        Icons.Filled.CropPortrait,
                        Icons.Outlined.CropPortrait,
                        UiText.StringResource(R.string.levelDetails_variant_portrait),
                    ),
                    ToggleIcon(
                        Icons.Filled.Crop169,
                        Icons.Outlined.Crop169,
                        UiText.StringResource(R.string.levelDetails_variant_landscape),
                    ),
                ),
                onSelected = {}
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewClusterSwatch() {
    ValolinkTheme {
        Surface {
            VariantPreviewCluster(
                variants = listOf("", "", "", ""),
                onSelected = {}
            )
        }
    }
}

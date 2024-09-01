package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop169
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.ui.theme.ValolinkTheme

@Composable
fun VariantPreviewCluster(
    modifier: Modifier = Modifier,
    variants: List<Any?> = emptyList(),
    onSelected: (Int) -> Unit,
) {
    var selected by remember {
        mutableIntStateOf(0)
    }

    if (variants.count() > 1) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                shadowElevation = 3.dp,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                ) {
                    variants.forEachIndexed { index, image ->
                        val isSelected = selected == index

                        if (image is ImageVector) {
                            FilledIconToggleButton(checked = isSelected, onCheckedChange = {
                                if (it) {
                                    selected = index
                                    onSelected(index)
                                }
                            }) {
                                Icon(imageVector = image, contentDescription = null)
                            }
                        }

                        if (image is String) {
                            ChromaToggleButton(
                                swatch = image,
                                checked = isSelected,
                                onCheckedChange = {
                                    if (it) {
                                        selected = index
                                        onSelected(index)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewClusterPlayerCard() {
    ValolinkTheme {
        Surface {
            VariantPreviewCluster(
                variants = listOf(Icons.Filled.CropPortrait, Icons.Filled.Crop169),
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
/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ConnectedButtonGroup.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.util.ToggleIcon
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.extensions.modifier.conditional

data class ConnectedButtonEntry(
    val label: UiText? = null,
    val icon: ToggleIcon? = null,
    val image: String? = null,
)

// FIXME: This is a temporary solution until an official implementation is present
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SingleConnectedButtonGroup(
    modifier: Modifier = Modifier,
    entries: List<ConnectedButtonEntry>,
    fillWidth: Boolean = true,
    initialSelection: Int = 0,
    onSelectedChanged: (index: Int) -> Unit,
) {
    var selectedIndex by remember { mutableIntStateOf(initialSelection) }

    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        entries.forEachIndexed { index, entry ->
            ToggleButton(
                enabled = entries.size > 1,
                checked = index == selectedIndex,
                onCheckedChange = {
                    onSelectedChanged(index)
                    selectedIndex = index
                },
                modifier = Modifier
                    .conditional(fillWidth) { weight(1f) }
                    .semantics {
                        role = Role.RadioButton
                    },
                shapes =
                    when (index) {
                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        entries.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                    }
            ) {
                if (entry.icon != null) {
                    Icon(
                        if (selectedIndex == index) entry.icon.active else entry.icon.inactive,
                        contentDescription = entry.icon.contentDescription?.asString()
                    )
                }

                if (entry.image != null) {
                    AsyncImage(
                        modifier = Modifier
                            .size(Spacing.xl)
                            .clip(CircleShape),
                        model = entry.image,
                        contentDescription = null,
                        placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_skin_chroma_swatch)
                    )
                }

                if ((entry.icon != null || entry.image != null) && entry.label != null) {
                    Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                }

                if (entry.label != null) {
                    Text(entry.label.asString())
                }
            }
        }
    }
}
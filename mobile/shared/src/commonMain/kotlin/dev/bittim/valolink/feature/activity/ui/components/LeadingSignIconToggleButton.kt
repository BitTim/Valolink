/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       LeadingSignIconToggleButton.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.08.26, 20:21
 */

package dev.bittim.valolink.feature.activity.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_rank_step_rr_sign_negative_desc
import valolink.shared.generated.resources.activity_add_flow_rank_step_rr_sign_positive_desc

@Composable
fun LeadingSignIconToggleButton(
    checked: Boolean,
    onCheckedChange: (checked: Boolean) -> Unit
) {
    var checked by rememberSaveable { mutableStateOf(checked) }

    IconToggleButton(
        checked = checked,
        onCheckedChange = {
            checked = it
            onCheckedChange(checked)
        }
    ) {
        AnimatedContent(
            targetState = checked
        ) {
            Icon(
                imageVector = if (it) Icons.Default.Remove else Icons.Default.Add,
                contentDescription = when (it) {
                    false -> stringResource(Res.string.activity_add_flow_rank_step_rr_sign_positive_desc)
                    true -> stringResource(Res.string.activity_add_flow_rank_step_rr_sign_negative_desc)
                }
            )
        }
    }
}
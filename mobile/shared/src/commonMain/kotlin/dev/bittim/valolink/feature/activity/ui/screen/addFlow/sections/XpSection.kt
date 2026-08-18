/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       XpSection.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.08.26, 20:35
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.feature.activity.ui.components.LeadingSignIconToggleButton
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_xp_step_xp_label
import kotlin.math.absoluteValue

/**
 * Provides an editable XP field with optional support for negative values.
 *
 * @param xp The initial XP value.
 * @param xpError The validation error message to display, if any.
 * @param allowNegative Whether the user can enter negative XP values.
 * @param onAction Receives an action whenever the XP value or sign changes.
 */
@Composable
fun XpSection(
    modifier: Modifier = Modifier,
    xp: Int?,
    xpError: String?,
    allowNegative: Boolean = false,
    onAction: (ActivityAddFlowAction) -> Unit,
) {
    var rawXp by rememberSaveable { mutableStateOf(when(allowNegative) {
            true -> xp?.toString() ?: ""
            false -> xp?.absoluteValue?.toString() ?: ""
    }) }
    var isNegative by rememberSaveable { mutableStateOf(false) }

    val action = {
        val sign = if (isNegative && allowNegative) "-" else ""
        onAction(ActivityAddFlowAction.XpChanged("$sign$rawXp", allowNegative = allowNegative))
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        OutlinedTextFieldWithError(
            value = rawXp,
            onValueChange = {
                rawXp = it
                action()
            },
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.activity_add_flow_xp_step_xp_label),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            error = xpError,
            leadingIcon = if (allowNegative) {
                {
                    LeadingSignIconToggleButton(
                        checked = isNegative,
                        onCheckedChange = {
                            isNegative = it
                            action()
                        }
                    )
                }
            } else null
        )
    }
}
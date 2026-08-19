/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RrSection.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 04:42
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.sections

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import dev.bittim.valolink.core.domain.model.MatchOutcome
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.feature.activity.ui.components.LeadingSignIconToggleButton
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_rank_step_rr_label
import kotlin.math.absoluteValue

@Composable
fun RrSection(
    modifier: Modifier = Modifier,
    visibleRrDelta: Int?,
    maxRrDigit: Int = 2,
    rrDeltaError: String?,
    matchOutcome: MatchOutcome?,
    onAction: (ActivityAddFlowAction) -> Unit,
) {
    var rawRrDelta by rememberSaveable(visibleRrDelta) { mutableStateOf(visibleRrDelta?.absoluteValue?.toString() ?: "") }
    var signChecked by rememberSaveable(visibleRrDelta, matchOutcome) {
        mutableStateOf(visibleRrDelta?.let { it < 0 } ?: (matchOutcome == MatchOutcome.Loss))
    }

    val action = {
        val sign = if (signChecked) "-" else ""
        onAction(ActivityAddFlowAction.RrDeltaChanged("$sign$rawRrDelta"))
    }

    OutlinedTextFieldWithError(
        value = rawRrDelta,
        onValueChange = {
            rawRrDelta = it.take(maxRrDigit)
            action()
        },
        modifier = modifier,
        label = stringResource(Res.string.activity_add_flow_rank_step_rr_label),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        error = rrDeltaError,
        leadingIcon = {
            LeadingSignIconToggleButton(
                checked = signChecked,
                onCheckedChange = {
                    signChecked = it
                    action()
                }
            )
        },
    )
}
/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RrRefundStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 04:46
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowAction
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.sections.RrSection
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_rr_refund_step_title
import valolink.shared.generated.resources.generic_button_finish

/**
 * Displays the RR refund step with the current refund value, validation state, and finish action.
 *
 * @param visibleRrDelta The RR refund value to display.
 * @param rrDeltaError The validation error associated with the RR refund value.
 * @param enableContinueButton Whether the Finish button is enabled.
 * @param onAction Handles actions emitted by the step.
 */
@Composable
fun RrRefundStep(
    modifier: Modifier = Modifier,
    visibleRrDelta: Int? = null,
    rrDeltaError: String? = null,
    enableContinueButton: Boolean = false,
    onAction: (ActivityAddFlowAction) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.l)
    ) {
        Text(
            text = stringResource(Res.string.activity_add_flow_rr_refund_step_title),
            style = MaterialTheme.typography.titleLarge
        )

        RrSection(
            modifier = Modifier.weight(1f),
            visibleRrDelta = visibleRrDelta,
            rrDeltaError = rrDeltaError,
            matchOutcome = null,
            onAction = onAction
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = enableContinueButton,
            onClick = { onAction(ActivityAddFlowAction.XpFinish) }
        ) {
            Text(text = stringResource(Res.string.generic_button_finish))
        }
    }
}

@Composable
@Preview
fun RrRefundStepPreview() {
    MaterialTheme {
        Surface {
            RrRefundStep(
                visibleRrDelta = 32,
                rrDeltaError = null,
                enableContinueButton = true,
                onAction = {}
            )
        }
    }
}
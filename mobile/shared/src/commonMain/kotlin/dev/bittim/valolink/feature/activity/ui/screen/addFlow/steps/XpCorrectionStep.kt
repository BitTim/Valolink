/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       XpCorrectionStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.08.26, 20:43
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
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.sections.XpSection
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.activity_add_flow_xp_correction_step_title
import valolink.shared.generated.resources.generic_button_finish

@Composable
fun XpCorrectionStep(
    modifier: Modifier = Modifier,
    xp: Int? = null,
    xpError: String? = null,
    enableContinueButton: Boolean = false,
    onAction: (ActivityAddFlowAction) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.l)
    ) {
        Text(
            text = stringResource(Res.string.activity_add_flow_xp_correction_step_title),
            style = MaterialTheme.typography.titleLarge
        )

        XpSection(
            modifier = Modifier.weight(1f),
            xp = xp,
            xpError = xpError,
            allowNegative = true,
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
fun XpCorrectionStepPreview() {
    MaterialTheme {
        Surface {
            XpCorrectionStep(
                xp = 1000,
                xpError = null,
                enableContinueButton = true,
                onAction = {}
            )
        }
    }
}
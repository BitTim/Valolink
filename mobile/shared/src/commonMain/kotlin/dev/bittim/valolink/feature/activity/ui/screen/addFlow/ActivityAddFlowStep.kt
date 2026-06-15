/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   11.06.26, 15:11
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import dev.bittim.valolink.core.ui.components.flowScaffold.FlowStep

enum class ActivityAddFlowStep(
    override val progress: Float
) : FlowStep {
    ModeStep(0f),
    MapStep(0.33f),
    ScoreStep(0.66f),
    OutcomeStep(1f),

    XpCorrectionStep(1f),
    RrRefundStep(1f);

    override val index: Int get() = ordinal
}
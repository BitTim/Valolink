/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.06.26, 01:56
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import dev.bittim.valolink.core.ui.components.flowScaffold.FlowStep

enum class ActivityAddFlowStep(
    override val progress: Float
) : FlowStep {
    ModeStep(0f),
    MapStep(0.2f),
    ScoreStep(0.4f),
    RankStep(0.8f),
    XpStep(1f),

    XpCorrectionStep(1f),
    RrRefundStep(1f);

    override val index: Int get() = ordinal
}
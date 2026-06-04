/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 12:17
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import dev.bittim.valolink.core.ui.components.flowScaffold.FlowStep

enum class ActivityAddFlowStep(
    override val progress: Float
) : FlowStep {
    ModeStep(0f),
    MapStep(1f / 5f);

    override val index: Int get() = ordinal
}
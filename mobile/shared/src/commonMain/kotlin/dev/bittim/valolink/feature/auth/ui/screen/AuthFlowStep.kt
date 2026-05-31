/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthFlowStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.05.26, 20:02
 */

package dev.bittim.valolink.feature.auth.ui.screen

import dev.bittim.valolink.core.ui.components.flowScaffold.FlowStep

enum class AuthFlowStep(
    override val progress: Float
) : FlowStep {
    LandingStep(progress = 0f),
    OtpStep(progress = 1f);

    override val index: Int get() = ordinal
}
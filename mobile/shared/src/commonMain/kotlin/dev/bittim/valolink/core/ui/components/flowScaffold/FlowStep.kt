/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FlowStep.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.05.26, 17:29
 */

package dev.bittim.valolink.core.ui.components.flowScaffold

interface FlowStep {
    val index: Int
    val progress: Float
}
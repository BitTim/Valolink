/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowAction.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   03.06.26, 13:14
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

sealed interface ActivityAddFlowAction {
    data class Back(val navBack: () -> Unit): ActivityAddFlowAction
}
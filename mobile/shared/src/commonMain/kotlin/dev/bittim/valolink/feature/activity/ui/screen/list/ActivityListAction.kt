/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListAction.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 20:35
 */

package dev.bittim.valolink.feature.activity.ui.screen.list

sealed interface ActivityListAction {
    data object Refresh : ActivityListAction
}
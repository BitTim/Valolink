/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.08.26, 17:12
 */

package dev.bittim.valolink.feature.activity.ui.screen.list.state

data class ActivityListState(
    val items: List<ActivityListItemState>? = null,
)

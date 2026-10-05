/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.09.26, 19:23
 */

package dev.bittim.valolink.feature.activity.ui.screen.list.state

sealed interface ActivityListState {
    data object Loading : ActivityListState
    data object Empty : ActivityListState
    data class Content(val items: List<ActivityListItemState>) : ActivityListState
    data object Error : ActivityListState
}
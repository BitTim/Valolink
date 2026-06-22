/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 16:24
 */

package dev.bittim.valolink.feature.activity.ui.screen.list

import dev.bittim.valolink.core.domain.model.Activity

data class ActivityListState(
    val activities: List<Activity>? = null
)

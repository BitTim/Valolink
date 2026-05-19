/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AgentOwnedStateConverter.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.05.26, 14:33
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.TypeConverter
import dev.bittim.valolink.core.domain.model.AgentOwnedState

class AgentOwnedStateConverter {
    @TypeConverter
    fun toAgentOwnedState(value: String?): AgentOwnedState? {
        return value?.let { AgentOwnedState.valueOf(it); }
    }

    @TypeConverter
    fun fromAgentOwnedState(value: AgentOwnedState?): String? {
        return value?.name
    }
}
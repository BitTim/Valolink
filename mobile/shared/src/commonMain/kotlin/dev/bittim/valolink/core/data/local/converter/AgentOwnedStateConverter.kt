/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AgentOwnedStateConverter.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
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
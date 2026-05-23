/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityTypeConverter.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.TypeConverter
import dev.bittim.valolink.core.domain.model.ActivityType

class ActivityTypeConverter {
    @TypeConverter
    fun toActivityType(value: String?): ActivityType? {
        return value?.let { ActivityType.valueOf(it); }
    }

    @TypeConverter
    fun fromActivityType(value: ActivityType?): String? {
        return value?.name
    }
}
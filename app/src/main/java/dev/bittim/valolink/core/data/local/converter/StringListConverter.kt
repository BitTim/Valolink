/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       StringListConverter.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@ProvidedTypeConverter
class StringListConverter(
    private val moshi: Moshi,
) {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return moshi.adapter<List<String>>(
            Types.newParameterizedType(
                List::class.java,
                String::class.java
            )
        ).toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return moshi.adapter<List<String>>(
            Types.newParameterizedType(
                List::class.java,
                String::class.java
            )
        ).fromJson(value) ?: emptyList()
    }
}
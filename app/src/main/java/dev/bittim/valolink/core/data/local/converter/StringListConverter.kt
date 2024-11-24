/*
Copyright (c) 2024 BitTim

Project:        Valolink
License:        GPLv3

File:           StringListConverter.kt
Author:         Tim Anhalt (BitTim)
Created:        23.05.2024
Description:    A type converter for lists of strings to be used with Room.
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
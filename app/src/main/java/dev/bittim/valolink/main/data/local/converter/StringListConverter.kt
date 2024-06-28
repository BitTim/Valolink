package dev.bittim.valolink.main.data.local.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@ProvidedTypeConverter
class StringListConverter constructor(
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
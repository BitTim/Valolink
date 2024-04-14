package dev.bittim.valolink.feature.content.data.local.game

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity

@OptIn(ExperimentalStdlibApi::class)
class GameConverter {
    private lateinit var moshi: Moshi //FixMe: Crash since var is not initialized when used

    @TypeConverter
    fun fromStringList(strings: List<String>?): String {
        return moshi.adapter<List<String>?>().toJson(strings)
    }

    @TypeConverter
    fun toStringList(json: String): List<String>? {
        return moshi.adapter<List<String>?>().fromJson(json)
    }

    @TypeConverter
    fun fromContractChapterList(chapters: List<ContractEntity.Content.Chapter>?): String {
        return moshi.adapter<List<ContractEntity.Content.Chapter>?>().toJson(chapters)
    }

    @TypeConverter
    fun toContractChapterList(json: String): List<ContractEntity.Content.Chapter>? {
        return moshi.adapter<List<ContractEntity.Content.Chapter>?>().fromJson(json)
    }
}
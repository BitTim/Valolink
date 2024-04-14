package dev.bittim.valolink.feature.content.data.local.game

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.bittim.valolink.feature.content.data.local.game.entity.SeasonEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity

@Database(
    entities = [
        SeasonEntity::class,
        ContractEntity::class
    ],
    version = 1
)
@TypeConverters(GameConverter::class)
abstract class GameDatabase : RoomDatabase() {
    abstract val dao: GameDao
}
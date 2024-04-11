package dev.bittim.valolink.feature.content.data.local.game

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.bittim.valolink.feature.content.data.local.game.entity.MapEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.SeasonEntity

@Database(
    entities = [
        SeasonEntity::class,
        MapEntity::class,
    ],
    version = 1
)
abstract class GameDatabase : RoomDatabase() {
    abstract val dao: GameDao
}
package dev.bittim.valolink.feature.content.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SeasonEntity::class],
    version = 1
)
abstract class GameDatabase : RoomDatabase() {
    abstract val dao: GameDao
}
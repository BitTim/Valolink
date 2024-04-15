package dev.bittim.valolink.feature.content.data.local.game

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.bittim.valolink.feature.content.data.local.game.entity.SeasonEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterLevelEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractContentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.RewardEntity

@Database(
    entities = [
        SeasonEntity::class,
        ContractEntity::class,
        ContractContentEntity::class,
        ChapterEntity::class,
        ChapterLevelEntity::class,
        RewardEntity::class
    ],
    version = 1
)
abstract class GameDatabase : RoomDatabase() {
    abstract val dao: GameDao
}
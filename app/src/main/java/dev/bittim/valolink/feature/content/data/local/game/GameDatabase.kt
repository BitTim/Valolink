package dev.bittim.valolink.feature.content.data.local.game

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.bittim.valolink.feature.content.data.local.game.entity.CurrencyEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.EventEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.SeasonEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.AbilityEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.AgentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.RecruitmentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.RoleEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.LevelEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.RewardEntity

@Database(
    entities = [
        SeasonEntity::class, EventEntity::class,
        
        ContractEntity::class, ContentEntity::class,
        ChapterEntity::class, LevelEntity::class, RewardEntity::class,

        AgentEntity::class, RecruitmentEntity::class, RoleEntity::class, AbilityEntity::class,

        CurrencyEntity::class
    ],
    version = 1
)
@TypeConverters(GameConverter::class)
abstract class GameDatabase : RoomDatabase() {
    abstract val dao: GameDao
}
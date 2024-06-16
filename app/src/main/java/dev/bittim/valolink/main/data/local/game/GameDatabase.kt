package dev.bittim.valolink.main.data.local.game

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.bittim.valolink.main.data.local.converter.StringListConverter
import dev.bittim.valolink.main.data.local.game.dao.AgentDao
import dev.bittim.valolink.main.data.local.game.dao.BuddyDao
import dev.bittim.valolink.main.data.local.game.dao.ContractsDao
import dev.bittim.valolink.main.data.local.game.dao.CurrencyDao
import dev.bittim.valolink.main.data.local.game.dao.EventDao
import dev.bittim.valolink.main.data.local.game.dao.PlayerCardDao
import dev.bittim.valolink.main.data.local.game.dao.PlayerTitleDao
import dev.bittim.valolink.main.data.local.game.dao.SeasonDao
import dev.bittim.valolink.main.data.local.game.dao.SprayDao
import dev.bittim.valolink.main.data.local.game.dao.VersionDao
import dev.bittim.valolink.main.data.local.game.dao.WeaponDao
import dev.bittim.valolink.main.data.local.game.entity.CurrencyEntity
import dev.bittim.valolink.main.data.local.game.entity.EventEntity
import dev.bittim.valolink.main.data.local.game.entity.PlayerCardEntity
import dev.bittim.valolink.main.data.local.game.entity.PlayerTitleEntity
import dev.bittim.valolink.main.data.local.game.entity.SeasonEntity
import dev.bittim.valolink.main.data.local.game.entity.SprayEntity
import dev.bittim.valolink.main.data.local.game.entity.VersionEntity
import dev.bittim.valolink.main.data.local.game.entity.agent.AbilityEntity
import dev.bittim.valolink.main.data.local.game.entity.agent.AgentEntity
import dev.bittim.valolink.main.data.local.game.entity.agent.RecruitmentEntity
import dev.bittim.valolink.main.data.local.game.entity.agent.RoleEntity
import dev.bittim.valolink.main.data.local.game.entity.buddy.BuddyEntity
import dev.bittim.valolink.main.data.local.game.entity.buddy.BuddyLevelEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.LevelEntity
import dev.bittim.valolink.main.data.local.game.entity.contract.RewardEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.WeaponEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.shopData.WeaponGridPositionEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.shopData.WeaponShopDataEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.skins.WeaponSkinChromaEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.skins.WeaponSkinEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.skins.WeaponSkinLevelEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.stats.WeaponAdsStatsEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.stats.WeaponAirBurstStatsEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.stats.WeaponAltShotgunStatsEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.stats.WeaponDamageRangeEntity
import dev.bittim.valolink.main.data.local.game.entity.weapon.stats.WeaponStatsEntity

@Database(
    entities = [
        VersionEntity::class,

        SeasonEntity::class, EventEntity::class,

        ContractEntity::class, ContentEntity::class, ChapterEntity::class,
        LevelEntity::class, RewardEntity::class,

        AgentEntity::class, RecruitmentEntity::class, RoleEntity::class, AbilityEntity::class,

        CurrencyEntity::class, SprayEntity::class, PlayerTitleEntity::class,
        PlayerCardEntity::class,

        BuddyEntity::class, BuddyLevelEntity::class,

        WeaponEntity::class, WeaponStatsEntity::class, WeaponAdsStatsEntity::class,
        WeaponAltShotgunStatsEntity::class, WeaponAirBurstStatsEntity::class,
        WeaponDamageRangeEntity::class, WeaponShopDataEntity::class,
        WeaponGridPositionEntity::class, WeaponSkinEntity::class,
        WeaponSkinChromaEntity::class, WeaponSkinLevelEntity::class
    ],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class GameDatabase : RoomDatabase() {
    abstract val versionDao: VersionDao

    abstract val agentDao: AgentDao
    abstract val contractsDao: ContractsDao
    abstract val eventDao: EventDao
    abstract val seasonDao: SeasonDao

    abstract val currencyDao: CurrencyDao
    abstract val sprayDao: SprayDao
    abstract val playerTitleDao: PlayerTitleDao
    abstract val playerCardDao: PlayerCardDao
    abstract val buddyDao: BuddyDao
    abstract val weaponDao: WeaponDao
}
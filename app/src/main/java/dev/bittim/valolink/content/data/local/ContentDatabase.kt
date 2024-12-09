package dev.bittim.valolink.content.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.bittim.valolink.content.data.local.dao.AgentDao
import dev.bittim.valolink.content.data.local.dao.BuddyDao
import dev.bittim.valolink.content.data.local.dao.ContractsDao
import dev.bittim.valolink.content.data.local.dao.CurrencyDao
import dev.bittim.valolink.content.data.local.dao.EventDao
import dev.bittim.valolink.content.data.local.dao.PlayerCardDao
import dev.bittim.valolink.content.data.local.dao.PlayerTitleDao
import dev.bittim.valolink.content.data.local.dao.SeasonDao
import dev.bittim.valolink.content.data.local.dao.SprayDao
import dev.bittim.valolink.content.data.local.dao.VersionDao
import dev.bittim.valolink.content.data.local.dao.WeaponDao
import dev.bittim.valolink.content.data.local.entity.CurrencyEntity
import dev.bittim.valolink.content.data.local.entity.EventEntity
import dev.bittim.valolink.content.data.local.entity.PlayerCardEntity
import dev.bittim.valolink.content.data.local.entity.PlayerTitleEntity
import dev.bittim.valolink.content.data.local.entity.SeasonEntity
import dev.bittim.valolink.content.data.local.entity.SprayEntity
import dev.bittim.valolink.content.data.local.entity.VersionEntity
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.agent.AbilityEntity
import dev.bittim.valolink.content.data.local.entity.agent.AgentEntity
import dev.bittim.valolink.content.data.local.entity.agent.RecruitmentEntity
import dev.bittim.valolink.content.data.local.entity.agent.RoleEntity
import dev.bittim.valolink.content.data.local.entity.buddy.BuddyEntity
import dev.bittim.valolink.content.data.local.entity.buddy.BuddyLevelEntity
import dev.bittim.valolink.content.data.local.entity.contract.ChapterEntity
import dev.bittim.valolink.content.data.local.entity.contract.ContentEntity
import dev.bittim.valolink.content.data.local.entity.contract.ContractEntity
import dev.bittim.valolink.content.data.local.entity.contract.LevelEntity
import dev.bittim.valolink.content.data.local.entity.contract.RewardEntity
import dev.bittim.valolink.content.data.local.entity.weapon.WeaponEntity
import dev.bittim.valolink.content.data.local.entity.weapon.shopData.WeaponGridPositionEntity
import dev.bittim.valolink.content.data.local.entity.weapon.shopData.WeaponShopDataEntity
import dev.bittim.valolink.content.data.local.entity.weapon.skins.WeaponSkinChromaEntity
import dev.bittim.valolink.content.data.local.entity.weapon.skins.WeaponSkinEntity
import dev.bittim.valolink.content.data.local.entity.weapon.skins.WeaponSkinLevelEntity
import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponAdsStatsEntity
import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponAirBurstStatsEntity
import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponAltShotgunStatsEntity
import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponDamageRangeEntity
import dev.bittim.valolink.content.data.local.entity.weapon.stats.WeaponStatsEntity
import dev.bittim.valolink.core.data.local.converter.StringListConverter
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [VersionEntity::class,

        SeasonEntity::class, EventEntity::class,

        ContractEntity::class, ContentEntity::class, ChapterEntity::class, LevelEntity::class, RewardEntity::class,

        AgentEntity::class, RecruitmentEntity::class, RoleEntity::class, AbilityEntity::class,

        CurrencyEntity::class, SprayEntity::class, PlayerTitleEntity::class, PlayerCardEntity::class,

        BuddyEntity::class, BuddyLevelEntity::class,

        WeaponEntity::class, WeaponStatsEntity::class, WeaponAdsStatsEntity::class, WeaponAltShotgunStatsEntity::class, WeaponAirBurstStatsEntity::class, WeaponDamageRangeEntity::class, WeaponShopDataEntity::class, WeaponGridPositionEntity::class, WeaponSkinEntity::class, WeaponSkinChromaEntity::class, WeaponSkinLevelEntity::class],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class ContentDatabase : RoomDatabase() {
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

    fun getAllOfType(type: String?): Flow<List<VersionedEntity?>> {
        return when (type) {
            "Agent" -> agentDao.getAll()
            "Contract" -> contractsDao.getAll()
            "Event" -> eventDao.getAll()
            "Season" -> seasonDao.getAll()

            "Currency" -> currencyDao.getAll()
            "Spray" -> sprayDao.getAll()
            "PlayerTitle" -> playerTitleDao.getAll()
            "PlayerCard" -> playerCardDao.getAll()
            "Buddy" -> buddyDao.getAll()
            "Weapon" -> weaponDao.getAll()

            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }
}
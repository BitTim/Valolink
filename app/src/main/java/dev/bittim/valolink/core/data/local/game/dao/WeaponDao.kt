package dev.bittim.valolink.core.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.game.entity.weapon.WeaponEntity
import dev.bittim.valolink.core.data.local.game.entity.weapon.shopData.WeaponGridPositionEntity
import dev.bittim.valolink.core.data.local.game.entity.weapon.shopData.WeaponShopDataEntity
import dev.bittim.valolink.core.data.local.game.entity.weapon.skins.WeaponSkinChromaEntity
import dev.bittim.valolink.core.data.local.game.entity.weapon.skins.WeaponSkinEntity
import dev.bittim.valolink.core.data.local.game.entity.weapon.skins.WeaponSkinLevelEntity
import dev.bittim.valolink.core.data.local.game.entity.weapon.stats.WeaponAdsStatsEntity
import dev.bittim.valolink.core.data.local.game.entity.weapon.stats.WeaponAirBurstStatsEntity
import dev.bittim.valolink.core.data.local.game.entity.weapon.stats.WeaponAltShotgunStatsEntity
import dev.bittim.valolink.core.data.local.game.entity.weapon.stats.WeaponDamageRangeEntity
import dev.bittim.valolink.core.data.local.game.entity.weapon.stats.WeaponStatsEntity
import dev.bittim.valolink.core.data.local.game.relation.weapon.WeaponSkinWithChromasAndLevels
import dev.bittim.valolink.core.data.local.game.relation.weapon.WeaponWithAllRelations
import kotlinx.coroutines.flow.Flow

@Dao
interface WeaponDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Transaction
	@Upsert
	suspend fun upsert(
		weapon: WeaponEntity,
		weaponStats: WeaponStatsEntity,
		weaponAdsStats: WeaponAdsStatsEntity,
		weaponAltShotgunStats: WeaponAltShotgunStatsEntity,
		weaponAirBurstStats: WeaponAirBurstStatsEntity,
		weaponDamageRanges: Set<WeaponDamageRangeEntity>,
		weaponShopData: WeaponShopDataEntity,
		weaponGridPosition: WeaponGridPositionEntity,
		weaponSkin: WeaponSkinEntity,
		weaponSkinChromas: Set<WeaponSkinChromaEntity>,
		weaponSkinLevels: Set<WeaponSkinLevelEntity>,
	)

	@Transaction
	@Upsert
	suspend fun upsert(
		weapons: Set<WeaponEntity>,
		weaponStats: Set<WeaponStatsEntity>,
		weaponAdsStats: Set<WeaponAdsStatsEntity>,
		weaponAltShotgunStats: Set<WeaponAltShotgunStatsEntity>,
		weaponAirBurstStats: Set<WeaponAirBurstStatsEntity>,
		weaponDamageRanges: Set<WeaponDamageRangeEntity>,
		weaponShopData: Set<WeaponShopDataEntity>,
		weaponGridPositions: Set<WeaponGridPositionEntity>,
		weaponSkins: Set<WeaponSkinEntity>,
		weaponSkinChromas: Set<WeaponSkinChromaEntity>,
		weaponSkinLevels: Set<WeaponSkinLevelEntity>,
	)

	// --------------------------------
	//  Query
	// --------------------------------

	@Transaction
	@Query("SELECT * FROM Weapons WHERE uuid = :uuid LIMIT 1")
	fun getByUuid(uuid: String): Flow<WeaponWithAllRelations?>

	@Transaction
	@Query("SELECT * FROM WeaponSkins WHERE uuid = :skinUuid LIMIT 1")
	fun getSkinByUuid(skinUuid: String): Flow<WeaponSkinWithChromasAndLevels?>

	@Transaction
	@Query(
		"""
        SELECT * FROM WeaponSkins
        WHERE uuid = (
            SELECT weaponSkin FROM WeaponSkinLevels
            WHERE uuid = :skinUuid
        ) LIMIT 1
    """
	)
	fun getSkinByLevelUuid(skinUuid: String): Flow<WeaponSkinWithChromasAndLevels?>

	@Transaction
	@Query("SELECT * FROM Weapons")
	fun getAll(): Flow<List<WeaponWithAllRelations>>
}
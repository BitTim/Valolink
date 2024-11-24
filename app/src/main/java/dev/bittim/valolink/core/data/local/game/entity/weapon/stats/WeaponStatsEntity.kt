package dev.bittim.valolink.core.data.local.game.entity.weapon.stats

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.core.data.local.game.entity.GameEntity
import dev.bittim.valolink.core.data.local.game.entity.weapon.WeaponEntity
import dev.bittim.valolink.main.domain.model.game.weapon.stats.WeaponAdsStats
import dev.bittim.valolink.main.domain.model.game.weapon.stats.WeaponAirBurstStats
import dev.bittim.valolink.main.domain.model.game.weapon.stats.WeaponAltShotgunStats
import dev.bittim.valolink.main.domain.model.game.weapon.stats.WeaponDamageRange
import dev.bittim.valolink.main.domain.model.game.weapon.stats.WeaponStats

@Entity(
	tableName = "WeaponStats", foreignKeys = [ForeignKey(
		entity = WeaponEntity::class,
		parentColumns = ["uuid"],
		childColumns = ["weapon"],
		onDelete = ForeignKey.CASCADE
	)], indices = [Index(
		value = ["uuid"], unique = true
	), Index(
		value = ["weapon"], unique = true
	)]
)
data class WeaponStatsEntity(
	@PrimaryKey val uuid: String,
	override val version: String,
	val weapon: String,
	val fireRate: Double,
	val magazineSize: Int,
	val runSpeedMultiplier: Double,
	val equipTimeSeconds: Double,
	val reloadTimeSeconds: Double,
	val firstBulletAccuracy: Double,
	val shotgunPelletCount: Int,
	val wallPenetration: String,
	val feature: String?,
	val fireMode: String?,
	val altFireType: String?,
) : GameEntity() {
	fun toType(
		adsStats: WeaponAdsStats?,
		altShotgunStats: WeaponAltShotgunStats?,
		airBurstStats: WeaponAirBurstStats?,
		damageRanges: List<WeaponDamageRange>,
	): WeaponStats {
		return WeaponStats(
			fireRate = fireRate,
			magazineSize = magazineSize,
			runSpeedMultiplier = runSpeedMultiplier,
			equipTimeSeconds = equipTimeSeconds,
			reloadTimeSeconds = reloadTimeSeconds,
			firstBulletAccuracy = firstBulletAccuracy,
			shotgunPelletCount = shotgunPelletCount,
			wallPenetration = wallPenetration,
			feature = feature,
			fireMode = fireMode,
			altFireType = altFireType,
			adsStats = adsStats,
			altShotgunStats = altShotgunStats,
			airBurstStats = airBurstStats,
			damageRanges = damageRanges
		)
	}
}

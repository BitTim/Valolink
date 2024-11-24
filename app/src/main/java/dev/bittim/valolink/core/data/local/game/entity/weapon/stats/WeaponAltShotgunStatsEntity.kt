package dev.bittim.valolink.core.data.local.game.entity.weapon.stats

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.core.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.weapon.stats.WeaponAltShotgunStats

@Entity(
	tableName = "WeaponAltShotgunStats", foreignKeys = [ForeignKey(
		entity = WeaponStatsEntity::class,
		parentColumns = ["uuid"],
		childColumns = ["weaponStats"],
		onDelete = ForeignKey.CASCADE
	)], indices = [Index(
		value = ["uuid"], unique = true
	), Index(
		value = ["weaponStats"], unique = true
	)]
)
data class WeaponAltShotgunStatsEntity(
	@PrimaryKey val uuid: String,
	override val version: String,
	val weaponStats: String,
	val shotgunPelletCount: Int,
	val burstRate: Double,
) : GameEntity() {
	fun toType(): WeaponAltShotgunStats {
		return WeaponAltShotgunStats(
			shotgunPelletCount = shotgunPelletCount,
			burstRate = burstRate,
		)
	}
}

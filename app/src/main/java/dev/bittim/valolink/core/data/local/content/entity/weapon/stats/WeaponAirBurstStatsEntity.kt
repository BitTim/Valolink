package dev.bittim.valolink.core.data.local.content.entity.weapon.stats

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.core.data.local.content.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.weapon.stats.WeaponAirBurstStats

@Entity(
	tableName = "WeaponAirBurstStats", foreignKeys = [ForeignKey(
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
data class WeaponAirBurstStatsEntity(
	@PrimaryKey val uuid: String,
	override val version: String,
	val weaponStats: String,
	val shotgunPelletCount: Int,
	val burstDistance: Double,
) : GameEntity() {
	fun toType(): WeaponAirBurstStats {
		return WeaponAirBurstStats(
			shotgunPelletCount = shotgunPelletCount,
			burstDistance = burstDistance,
		)
	}
}

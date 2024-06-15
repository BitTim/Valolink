package dev.bittim.valolink.main.data.local.game.entity.weapon.stats

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.weapon.stats.WeaponAdsStats

@Entity(
    tableName = "WeaponAdsStats",
    foreignKeys = [ForeignKey(
        entity = WeaponStatsEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["weaponStats"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(
        value = ["uuid"],
        unique = true
    ), Index(
        value = ["weaponStats"],
        unique = true
    )]
)
data class WeaponAdsStatsEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val weaponStats: String,
    val zoomMultiplier: Double,
    val fireRate: Double,
    val runSpeedMultiplier: Double,
    val burstCount: Int,
    val firstBulletAccuracy: Double,
) : GameEntity() {
    fun toType(): WeaponAdsStats {
        return WeaponAdsStats(
            zoomMultiplier = zoomMultiplier,
            fireRate = fireRate,
            runSpeedMultiplier = runSpeedMultiplier,
            burstCount = burstCount,
            firstBulletAccuracy = firstBulletAccuracy,
        )
    }
}
package dev.bittim.valolink.main.data.local.game.entity.weapon.stats

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.weapon.stats.WeaponDamageRange

@Entity(
    tableName = "WeaponDamageRanges",
    foreignKeys = [
        ForeignKey(
            entity = WeaponStatsEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["weaponStats"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["uuid"],
            unique = true
        ),
        Index(
            value = ["weaponStats"],
            unique = true
        )
    ]
)
data class WeaponDamageRangeEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val weaponStats: String,
    val rangeStartMeters: Float,
    val rangeEndMeters: Float,
    val headDamage: Float,
    val bodyDamage: Float,
    val legDamage: Float,
) : GameEntity() {
    fun toType(): WeaponDamageRange {
        return WeaponDamageRange(
            rangeStartMeters = rangeStartMeters,
            rangeEndMeters = rangeEndMeters,
            headDamage = headDamage,
            bodyDamage = bodyDamage,
            legDamage = legDamage,
        )
    }
}

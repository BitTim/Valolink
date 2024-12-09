package dev.bittim.valolink.content.data.local.entity.weapon.shopData

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.GameEntity
import dev.bittim.valolink.content.domain.model.weapon.shopData.WeaponGridPosition

@Entity(
	tableName = "WeaponGridPositions", foreignKeys = [ForeignKey(
		entity = WeaponShopDataEntity::class,
		parentColumns = ["uuid"],
		childColumns = ["weaponShopData"],
		onDelete = ForeignKey.CASCADE
	)], indices = [Index(
		value = ["uuid"], unique = true
	), Index(
		value = ["weaponShopData"], unique = true
	)]
)
data class WeaponGridPositionEntity(
	@PrimaryKey val uuid: String,
	override val version: String,
	val weaponShopData: String,
	val row: Int,
	val column: Int,
) : GameEntity() {
	fun toType(): WeaponGridPosition {
		return WeaponGridPosition(
			row = row,
			column = column,
		)
	}
}

package dev.bittim.valolink.core.data.local.game.entity.weapon.shopData

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.core.data.local.game.entity.GameEntity
import dev.bittim.valolink.core.data.local.game.entity.weapon.WeaponEntity
import dev.bittim.valolink.main.domain.model.game.weapon.shopData.WeaponGridPosition
import dev.bittim.valolink.main.domain.model.game.weapon.shopData.WeaponShopData

@Entity(
	tableName = "WeaponShopData", foreignKeys = [ForeignKey(
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
data class WeaponShopDataEntity(
	@PrimaryKey val uuid: String,
	override val version: String,
	val weapon: String,
	val cost: Int,
	val category: String,
	val shopOrderPriority: Int,
	val categoryText: String,
	val canBeTrashed: Boolean,
	val image: String?,
	val newImage: String,
	val newImage2: String?,
) : GameEntity() {
	fun toType(
		gridPosition: WeaponGridPosition?,
	): WeaponShopData {
		return WeaponShopData(
			cost = cost,
			category = category,
			shopOrderPriority = shopOrderPriority,
			categoryText = categoryText,
			gridPosition = gridPosition,
			canBeTrashed = canBeTrashed,
			image = image,
			newImage = newImage,
			newImage2 = newImage2,
		)
	}
}

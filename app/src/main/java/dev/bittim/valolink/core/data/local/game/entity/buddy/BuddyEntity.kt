package dev.bittim.valolink.core.data.local.game.entity.buddy

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.core.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.buddy.Buddy
import dev.bittim.valolink.main.domain.model.game.buddy.BuddyLevel

@Entity(
	tableName = "Buddies",
	indices = [
		Index(
			value = ["uuid"], unique = true
		),
	]
)
data class BuddyEntity(
	@PrimaryKey val uuid: String,
	override val version: String,
	val displayName: String,
	val isHiddenIfNotOwned: Boolean,
	val themeUuid: String?,
	val displayIcon: String,
	val assetPath: String,
) : GameEntity() {
	fun toType(levels: List<BuddyLevel>): Buddy {
		return Buddy(
			uuid = uuid,
			displayName = displayName,
			isHiddenIfNotOwned = isHiddenIfNotOwned,
			themeUuid = themeUuid,
			displayIcon = displayIcon,
			levels = levels
		)
	}
}

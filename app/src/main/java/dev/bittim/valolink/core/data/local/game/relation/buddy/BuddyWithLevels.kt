package dev.bittim.valolink.core.data.local.game.relation.buddy

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.core.data.local.game.entity.VersionedEntity
import dev.bittim.valolink.core.data.local.game.entity.buddy.BuddyEntity
import dev.bittim.valolink.core.data.local.game.entity.buddy.BuddyLevelEntity
import dev.bittim.valolink.main.domain.model.game.buddy.Buddy

data class BuddyWithLevels(
	@Embedded val buddy: BuddyEntity,
	@Relation(
		parentColumn = "uuid", entityColumn = "buddy"
	) val levels: List<BuddyLevelEntity>,
) : VersionedEntity {
	override fun getApiVersion(): String {
		return buddy.version
	}

	fun toType(): Buddy {
		return buddy.toType(levels.map { it.toType() })
	}
}

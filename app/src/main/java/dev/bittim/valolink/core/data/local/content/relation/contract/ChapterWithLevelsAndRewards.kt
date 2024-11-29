package dev.bittim.valolink.core.data.local.content.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.core.data.local.content.entity.VersionedEntity
import dev.bittim.valolink.core.data.local.content.entity.contract.ChapterEntity
import dev.bittim.valolink.core.data.local.content.entity.contract.LevelEntity
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Chapter
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardRelation

data class ChapterWithLevelsAndRewards(
	@Embedded val chapter: ChapterEntity,
	@Relation(
		entity = LevelEntity::class, parentColumn = "uuid", entityColumn = "chapterUuid"
	) val levels: List<LevelWithRewards>,
) : VersionedEntity {
	override fun getApiVersion(): String {
		return chapter.version
	}

	fun toType(
		rewards: List<List<RewardRelation?>>,
		levelNames: List<String>,
	): Chapter {
		return chapter.toType(levels.mapIndexed { index, level ->
			level.toType(
				rewards.getOrNull(index) ?: emptyList(),
				levelNames[index],
			)
		})
	}
}
package dev.bittim.valolink.content.data.local.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.contract.ChapterEntity
import dev.bittim.valolink.content.data.local.entity.contract.ContentEntity
import dev.bittim.valolink.content.domain.model.contract.content.Content
import dev.bittim.valolink.content.domain.model.contract.content.ContentRelation
import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation

data class ContentWithChaptersWithLevelsAndRewards(
	@Embedded val content: ContentEntity,
	@Relation(
		entity = ChapterEntity::class, parentColumn = "uuid", entityColumn = "contentUuid"
	) val chapters: List<ChapterWithLevelsAndRewards>,
) : VersionedEntity {
	override fun getApiVersion(): String {
		return content.version
	}

	fun toType(
		relation: ContentRelation?,
		rewards: List<List<List<RewardRelation?>>>,
		levelNames: List<List<String>>,
	): Content {
		return content.toType(relation, chapters.mapIndexed { index, rawChapter ->
			rawChapter.toType(rewards.getOrNull(index) ?: emptyList(), levelNames[index])
		})
	}
}
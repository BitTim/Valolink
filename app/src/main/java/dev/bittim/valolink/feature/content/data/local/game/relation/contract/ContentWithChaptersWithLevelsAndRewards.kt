package dev.bittim.valolink.feature.content.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.feature.content.domain.model.contract.Content
import dev.bittim.valolink.feature.content.domain.model.contract.ContentRelation

data class ContentWithChaptersWithLevelsAndRewards(
    @Embedded val content: ContentEntity,
    @Relation(
        entity = ChapterEntity::class,
        parentColumn = "uuid",
        entityColumn = "contentUuid"
    )
    val chapters: List<ChapterWithLevelsAndRewards>
) {
    fun toType(relation: ContentRelation?): Content {
        return content.toType(relation, chapters.map { it.toType() })
    }
}

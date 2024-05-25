package dev.bittim.valolink.feature.main.data.local.game.relation.contract

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.feature.main.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.main.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.feature.main.domain.model.game.contract.Content
import dev.bittim.valolink.feature.main.domain.model.game.contract.ContentRelation

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

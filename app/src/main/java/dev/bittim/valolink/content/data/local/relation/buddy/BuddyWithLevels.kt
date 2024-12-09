package dev.bittim.valolink.content.data.local.relation.buddy

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.buddy.BuddyEntity
import dev.bittim.valolink.content.data.local.entity.buddy.BuddyLevelEntity
import dev.bittim.valolink.content.domain.model.buddy.Buddy

data class BuddyWithLevels(
    @Embedded val buddy: BuddyEntity,
    @Relation(
        parentColumn = "uuid", entityColumn = "buddy"
    ) val levels: List<BuddyLevelEntity>,
) : VersionedEntity {
    override val version: String
        get() = buddy.version

    fun toType(): Buddy {
        return buddy.toType(levels.map { it.toType() })
    }
}

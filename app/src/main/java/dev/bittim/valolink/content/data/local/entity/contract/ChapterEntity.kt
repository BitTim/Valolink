/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ChapterEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.local.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.contract.chapter.Chapter
import dev.bittim.valolink.content.domain.model.contract.chapter.Level

@Entity(
    tableName = "ContractChapters",
    foreignKeys = [ForeignKey(
        entity = ContentEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["contentUuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(
            value = ["uuid"], unique = true
        ),
        Index(
            value = ["contentUuid"], unique = false
        ),
    ]
)
data class ChapterEntity(
    @PrimaryKey val uuid: String,
    val contentUuid: String,
    override val version: String,
    val isEpilogue: Boolean,
) : VersionedEntity {
    fun toType(
        levels: List<Level>,
    ): Chapter {
        return Chapter(
            levels, isEpilogue
        )
    }
}
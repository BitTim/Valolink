/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ContentEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.entity.contract

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.contract.chapter.Chapter
import dev.bittim.valolink.content.domain.model.contract.content.Content
import dev.bittim.valolink.content.domain.model.contract.content.ContentRelation

@Entity(
    tableName = "ContractContents", foreignKeys = [ForeignKey(
        entity = ContractEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["contractUuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["contractUuid"], unique = true
    )]
)
data class ContentEntity(
    @PrimaryKey override val uuid: String,
    val contractUuid: String,
    override val version: String,
    val relationType: String?,
    val relationUuid: String?,
    val premiumRewardScheduleUuid: String?,
    val premiumVPCost: Int,
) : VersionedEntity {
    fun toType(
        relation: ContentRelation?,
        chapters: List<Chapter>,
    ): Content {
        return Content(
            relation, premiumVPCost, chapters
        )
    }
}
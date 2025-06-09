/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MapWithCallouts.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.data.local.entity.map.MapCalloutEntity
import dev.bittim.valolink.content.data.local.entity.map.MapEntity
import dev.bittim.valolink.content.domain.model.map.GameMap

data class MapWithCallouts(
    @Embedded val map: MapEntity,
    @Relation(
        parentColumn = "uuid", entityColumn = "map"
    ) val callouts: Set<MapCalloutEntity>,
) : VersionedEntity {
    override val uuid: String
        get() = map.uuid
    override val version: String
        get() = map.version

    fun toType(): GameMap {
        return map.toType(
            callouts.map { it.toType() }
        )
    }
}

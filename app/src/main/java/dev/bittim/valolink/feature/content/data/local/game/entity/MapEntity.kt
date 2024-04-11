package dev.bittim.valolink.feature.content.data.local.game.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.domain.model.Location
import dev.bittim.valolink.feature.content.domain.model.Map

@Entity
data class MapEntity(
    @PrimaryKey
    val uuid: String,
    override val version: String,
    val displayName: String,
    val narrativeDescription: String,
    val tacticalDescription: String,
    val coordinates: String,
    val displayIcon: String,
    val listViewIcon: String,
    val listViewIconTall: String,
    val splash: String,
    val stylizedBackgroundImage: String,
    val premierBackgroundImage: String,
    val assetPath: String,
    val mapUrl: String,
    val xMultiplier: Double,
    val xScalarToAdd: Double,
    val yMultiplier: Double,
    val yScalarToAdd: Double,
    //val callouts: List<Callout> //TODO: Implement this
) : GameEntity<Map>() {
    data class Callout(
        val regionName: String,
        val superRegionName: String,
        val location: Location
    )

    override fun toType(): Map {
        return Map(
            uuid = uuid,
            name = displayName,
            description = narrativeDescription,
            coordinates = coordinates
        )
    }
}
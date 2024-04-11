package dev.bittim.valolink.feature.content.data.remote.game.dto

import dev.bittim.valolink.feature.content.data.local.game.entity.MapEntity
import dev.bittim.valolink.feature.content.domain.model.Location

data class MapDto(
    val uuid: String,
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
    val callouts: List<Callout>
) : GameDto<MapEntity>() {
    data class Callout(
        val regionName: String,
        val superRegionName: String,
        val location: Location
    )

    override fun toEntity(version: String): MapEntity {
        return MapEntity(
            uuid = uuid,
            version = version,
            displayName = displayName,
            narrativeDescription = narrativeDescription,
            tacticalDescription = tacticalDescription,
            coordinates = coordinates,
            displayIcon = displayIcon,
            listViewIcon = listViewIcon,
            listViewIconTall = listViewIconTall,
            splash = splash,
            stylizedBackgroundImage = stylizedBackgroundImage,
            premierBackgroundImage = premierBackgroundImage,
            assetPath = assetPath,
            mapUrl = mapUrl,
            xMultiplier = xMultiplier,
            yMultiplier = yMultiplier,
            xScalarToAdd = xScalarToAdd,
            yScalarToAdd = yScalarToAdd,
            //callouts = callouts.map { MapEntity.Callout(it.regionName, it.superRegionName, it.location) }
        )
    }
}
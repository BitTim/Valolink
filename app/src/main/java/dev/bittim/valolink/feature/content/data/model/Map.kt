package dev.bittim.valolink.feature.content.data.model

data class Map(
    val uuid: String,
    val name: String,
    val description: String,
    val layoutImageUrl: String,
    val listViewImageUrl: String,
    val tallListViewImageUrl: String,
    val splashImageUrl: String,
    val coordinateMultiplier: Vector2,
    val coordinateScalar: Vector2,
    val callouts: List<MapCallout>
)

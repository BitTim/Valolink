package dev.bittim.valolink.feature.content.domain.model

data class Location(
    val x: Double,
    val y: Double
) {
    companion object {
        val EMPTY: Location = Location(0.0, 0.0)
    }
}
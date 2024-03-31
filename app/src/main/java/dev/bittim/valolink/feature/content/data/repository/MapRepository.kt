package dev.bittim.valolink.feature.content.data.repository

import dev.bittim.valolink.feature.content.data.model.Map

interface MapRepository {
    fun getMaps(): List<Map>
    fun getMapByUuid(uuid: String): Map?
}
/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankTableDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.content.data.remote.dto.rank

import dev.bittim.valolink.content.data.local.entity.rank.RankTableEntity

data class RankTableDto(
    val uuid: String,
    val assetObjectName: String,
    val tiers: List<RankDto>,
    val assetPath: String,
) {
    fun toEntity(version: String): RankTableEntity {
        return RankTableEntity(
            uuid,
            version,
            // This gets the Episode number from the assetObjectName
            // Example: "Episode5_CompetitiveTierDataTable" -> 5
            assetObjectName.split('_')[0].filter { it.isDigit() }.toInt()
        )
    }
}

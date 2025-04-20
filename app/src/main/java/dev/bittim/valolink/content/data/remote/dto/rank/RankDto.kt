/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.content.data.remote.dto.rank

import dev.bittim.valolink.content.data.local.entity.rank.RankEntity

data class RankDto(
    val tier: Int,
    val tierName: String,
    val division: String,
    val divisionName: String,
    val color: String,
    val backgroundColor: String,
    val smallIcon: String?,
    val largeIcon: String?,
    val rankTriangleDownIcon: String?,
    val rankTriangleUpIcon: String?,
) {
    fun toEntity(rankTable: String, version: String): RankEntity {
        return RankEntity(
            uuid = "${rankTable}_$tier",
            version = version,
            rankTable = rankTable,
            tier = tier,
            name = tierName,
            divisionName = divisionName,
            color = color,
            backgroundColor = backgroundColor,
            icon = largeIcon,
            triangleDownIcon = rankTriangleDownIcon,
            triangleUpIcon = rankTriangleUpIcon
        )
    }
}

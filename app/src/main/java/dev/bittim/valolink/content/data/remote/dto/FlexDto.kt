/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       FlexDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.05.25, 13:39
 */

package dev.bittim.valolink.content.data.remote.dto

import dev.bittim.valolink.content.data.local.entity.FlexEntity

data class FlexDto(
    val uuid: String,
    val displayName: String,
    val displayNameAllCaps: String,
    val displayIcon: String,
    val assetPath: String,
) {
    fun toEntity(version: String): FlexEntity {
        return FlexEntity(
            uuid, version, displayName, displayIcon
        )
    }
}

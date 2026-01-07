/*
 Copyright (c) 2025-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ModeDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   07.01.26, 01:16
 */

package dev.bittim.valolink.content.data.remote.dto.mode

import dev.bittim.valolink.content.data.local.entity.ModeEntity
import dev.bittim.valolink.content.domain.model.map.MapType
import dev.bittim.valolink.content.domain.model.mode.ScoreType

data class ModeDto(
    val uuid: String,
    val displayName: String,
    val description: String?,
    val duration: String?,
    val economyType: String?,
    val allowsMatchTimeouts: Boolean,
    val isTeamVoiceAllowed: Boolean,
    val isMinimapHidden: Boolean,
    val orbCount: Int,
    val roundsPerHalf: Int,
    val teamRoles: List<String>?,
    val gameFeatureOverrides: List<GameFeatureOverrideDto>?,
    val gameRuleBoolOverrides: List<GameRuleBoolOverrideDto>?,
    val displayIcon: String?,
    val listViewIconTall: String?,
    val assetPath: String,
) {
    fun toEntity(version: String): ModeEntity {
        val scoreType = when {
            assetPath.contains("/NPEV2_GameMode") ||
                    assetPath.contains("/NPEGameMode") ||
                    assetPath.contains("/ShootingRangeGameMode") -> ScoreType.None

            assetPath.contains("/DeathmatchGameMode") ||
                    assetPath.contains("/SnowballFightGameMode") -> ScoreType.Placement

            else -> ScoreType.Default
        }

        val mapType = when {
            assetPath.contains("/HURM") -> MapType.TDM
            assetPath.contains("/NPEV2_GameMode") ||
                    assetPath.contains("/NPEGameMode") -> MapType.Tutorial

            assetPath.contains("/ShootingRangeGameMode") -> MapType.Range
            else -> MapType.Default
        }

        return ModeEntity(
            uuid,
            version,
            displayName,
            description,
            scoreType,
            mapType,
            assetPath.contains("/BombGameMode"),
            duration,
            roundsPerHalf,
            displayIcon,
            listViewIconTall
        )
    }
}
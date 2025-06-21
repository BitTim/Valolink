/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankChangeResult.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.06.25, 22:18
 */

package dev.bittim.valolink.core.domain.model

data class RankChangeResult(
    val newTier: Int,
    val newRR: Int,
    val shadowDeltaRR: Int,
)

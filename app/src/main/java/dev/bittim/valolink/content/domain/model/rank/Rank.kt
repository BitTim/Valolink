/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       Rank.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.content.domain.model.rank

data class Rank(
    val tier: Int,
    val name: String,
    val divisionName: String,
    val color: String,
    val backgroundColor: String,
    val icon: String?,
    val triangleDownIcon: String?,
    val triangleUpIcon: String?,
)

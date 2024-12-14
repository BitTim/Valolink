/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Recruitment.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.domain.model.agent

data class Recruitment(
    val uuid: String,
    val xp: Int,
    val useLevelVpCostOverride: Boolean,
    val levelVpCostOverride: Int,
    val startDate: String,
    val endDate: String,
) {
    companion object {
        val EMPTY = Recruitment(
            uuid = "",
            xp = 0,
            useLevelVpCostOverride = false,
            levelVpCostOverride = 0,
            startDate = "",
            endDate = ""
        )
    }
}
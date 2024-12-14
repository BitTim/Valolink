/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Role.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.domain.model.agent

data class Role(
    val uuid: String,
    val displayName: String,
    val description: String,
    val displayIcon: String,
) {
    companion object {
        val EMPTY = Role(
            uuid = "",
            displayName = "",
            description = "",
            displayIcon = ""
        )
    }
}

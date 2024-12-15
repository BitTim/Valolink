/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       BuddyLevel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.domain.model.buddy

data class BuddyLevel(
    val uuid: String,
    val hideIfNotOwned: Boolean,
    val displayName: String,
    val displayIcon: String,
) {
    companion object {
        val EMPTY = BuddyLevel(
            uuid = "",
            hideIfNotOwned = false,
            displayName = "",
            displayIcon = ""
        )
    }
}

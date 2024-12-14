/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ContentType.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:30
 */

package dev.bittim.valolink.content.domain.model.contract.content

enum class ContentType(
    val displayName: String,
    val internalName: String,
) {
    SEASON("Season", "Season"),
    EVENT("Event", "Event"),
    AGENT("Recruit", "Agent")
}
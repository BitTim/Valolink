/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserLevel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   25.04.25, 04:25
 */

package dev.bittim.valolink.user.domain.model

data class UserLevel(
    val uuid: String,
    val userContract: String,
    val level: String,
    val isPurchased: Boolean,
    val xpOffset: Int?
)

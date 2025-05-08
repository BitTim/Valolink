/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserContract.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   25.04.25, 19:03
 */

package dev.bittim.valolink.user.domain.model

data class UserContract(
    val uuid: String,
    val user: String,
    val contract: String,
    val levels: List<UserLevel>,
    val freeOnly: Boolean,
)

/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       UserContract.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.user.domain.model

data class UserContract(
    val uuid: String,
    val user: String,
    val contract: String,
    val levels: List<UserLevel>,
)

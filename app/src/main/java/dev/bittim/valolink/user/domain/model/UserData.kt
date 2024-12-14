/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       UserData.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.user.domain.model

data class UserData(
    val uuid: String,
    val isPrivate: Boolean,
    val username: String,
    val agents: List<UserAgent>,
    val contracts: List<UserContract>,
)

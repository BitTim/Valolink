/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserData.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:40
 */

package dev.bittim.valolink.user.domain.model

data class UserData(
    val uuid: String,
    val isPrivate: Boolean,
    val username: String,
    val onboardingStep: Int,
    val avatar: String?,
    val agents: List<UserAgent>,
    val contracts: List<UserContract>,
)

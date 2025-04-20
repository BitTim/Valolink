/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserData.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
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
    val rank: UserRank?,
) {
    companion object {
        fun empty(uid: String): UserData {
            return UserData(
                uuid = uid,
                isPrivate = true,
                username = "",
                onboardingStep = 0,
                avatar = null,
                agents = emptyList(),
                contracts = emptyList(),
                rank = null,
            )
        }
    }
}

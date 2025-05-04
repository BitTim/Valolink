/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserMeta.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 10:55
 */

package dev.bittim.valolink.user.domain.model

data class UserMeta(
    val uuid: String,
    val isPrivate: Boolean,
    val username: String,
    val onboardingStep: Int,
    val avatar: String?,
    val rank: UserRank?,
) {
    companion object {
        fun empty(uid: String): UserMeta {
            return UserMeta(
                uuid = uid,
                isPrivate = true,
                username = "",
                onboardingStep = 0,
                avatar = null,
                rank = null,
            )
        }
    }
}

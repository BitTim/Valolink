/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserRank.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   23.04.25, 00:08
 */

package dev.bittim.valolink.user.domain.model

data class UserRank(
    val uuid: String,
    val tier: Int,
    val rr: Int,
    val matchesPlayed: Int,
    val matchesNeeded: Int,
) {
    companion object {
        fun empty(uid: String): UserRank {
            return UserRank(
                uuid = uid,
                tier = 0,
                rr = 0,
                matchesPlayed = 0,
                matchesNeeded = 0,
            )
        }
    }
}

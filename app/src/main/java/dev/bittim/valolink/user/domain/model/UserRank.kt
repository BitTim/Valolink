/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserRank.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.user.domain.model

import java.util.UUID

data class UserRank(
    val uuid: String,
    val user: String,
    val tier: Int,
    val rr: Int,
    val matchesPlayed: Int,
    val matchesNeeded: Int,
) {
    companion object {
        fun empty(uid: String): UserRank {
            return UserRank(
                uuid = UUID.randomUUID().toString(),
                user = uid,
                tier = 0,
                rr = 0,
                matchesPlayed = 0,
                matchesNeeded = 0,
            )
        }
    }
}

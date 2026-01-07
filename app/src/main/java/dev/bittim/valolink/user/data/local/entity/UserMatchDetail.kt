/*
 Copyright (c) 2025-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserMatchDetail.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   07.01.26, 01:16
 */

package dev.bittim.valolink.user.data.local.entity

import androidx.room.PrimaryKey


data class UserMatchDetail(
    @PrimaryKey override val uuid: String,
    override val isSynced: Boolean,
    override val toDelete: Boolean,
    override val updatedAt: String,
    val timestamp: String,
    val map: String,
    val mode: String,
    val aScore: Int,
    val bScore: Int?,
    val aSurrender: Boolean,
    val bSurrender: Boolean,
) : SyncedEntity {
    override fun getIdentifier(): String {
        TODO("Not yet implemented")
    }

    override fun withIsSynced(isSynced: Boolean): SyncedEntity {
        TODO("Not yet implemented")
    }
}

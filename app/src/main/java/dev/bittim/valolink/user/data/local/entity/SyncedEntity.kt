/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       SyncedEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:40
 */

package dev.bittim.valolink.user.data.local.entity

interface SyncedEntity {
    val uuid: String
    val isSynced: Boolean
    val toDelete: Boolean
    val updatedAt: String

    fun getIdentifier(): String
    fun withIsSynced(isSynced: Boolean): SyncedEntity
}

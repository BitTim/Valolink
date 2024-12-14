/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SyncedDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.user.data.remote.dto

abstract class SyncedDto<T> {
    abstract val uuid: String
    abstract val updatedAt: String
    abstract fun getIdentifier(): String

    abstract fun toEntity(isSynced: Boolean, toDelete: Boolean): T
}

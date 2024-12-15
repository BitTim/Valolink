/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       UserRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.user.data.repository.data

interface UserRepository<T, D> {
    suspend fun remoteQuery(relation: String): List<D>?
    suspend fun remoteUpsert(uuid: String): Boolean
    suspend fun remoteDelete(uuid: String): Boolean
}
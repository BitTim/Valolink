/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       VersionRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.repository.version

import dev.bittim.valolink.content.domain.model.Version
import kotlinx.coroutines.flow.Flow

interface VersionRepository {
    suspend fun get(): Flow<Version>
}
/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       BuddyRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.repository.buddy

import dev.bittim.valolink.content.data.repository.ContentRepository
import dev.bittim.valolink.content.domain.model.buddy.Buddy
import kotlinx.coroutines.flow.Flow

interface BuddyRepository : ContentRepository<Buddy> {
    suspend fun getByLevelUuid(levelUuid: String): Flow<Buddy?>
}
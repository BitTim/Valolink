/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoVersionRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 13:53
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.ValoVersion
import kotlinx.coroutines.flow.Flow

interface ValoVersionRepo {
    fun observe(): Flow<ValoVersion?>
    fun observeRemote(): Flow<ValoVersion>

    suspend fun upsert(version: ValoVersion)
}
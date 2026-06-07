/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoModeRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 16:27
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.ValoMode
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface ValoModeRepo {
    fun observe(uuid: Uuid, locale: String? = null): Flow<ValoMode?>
    fun observeAll(locale: String? = null): Flow<List<ValoMode>>

    suspend fun sync()
}
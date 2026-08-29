/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMapRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 16:22
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.SimpleValoMap
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface ValoMapRepo {
    suspend fun get(uuid: Uuid, locale: String? = null): SimpleValoMap?
    fun observe(uuid: Uuid, locale: String? = null): Flow<SimpleValoMap?>
    fun observeAll(locale: String? = null): Flow<List<SimpleValoMap>>

    suspend fun sync()
}
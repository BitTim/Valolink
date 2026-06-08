/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoMapRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 19:32
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.SimpleValoMap
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface ValoMapRepo {
    fun observe(uuid: Uuid, locale: String? = null): Flow<SimpleValoMap?>
    fun observeAll(locale: String? = null): Flow<List<SimpleValoMap>>

    suspend fun sync()
}
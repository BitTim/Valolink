/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoModeRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 13:13
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.ValoMode
import kotlinx.coroutines.flow.Flow

interface ValoModeRepo {
    fun observe(locale: String? = null): Flow<ValoMode?>
    fun observeAll(locale: String? = null): Flow<List<ValoMode>>

    fun sync()
}
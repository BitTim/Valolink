/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SyncManager.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 04:16
 */

package dev.bittim.valolink.core.data.sync

import dev.bittim.valolink.core.domain.repo.ValoMapRepo
import dev.bittim.valolink.core.domain.repo.ValoModeRepo
import dev.bittim.valolink.core.domain.repo.ValoSeasonRepo
import dev.bittim.valolink.core.domain.repo.ValoVersionRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn

class SyncManager(
    private val scope: CoroutineScope,
    private val valoVersionRepo: ValoVersionRepo,
    private val valoModeRepo: ValoModeRepo,
    private val valoMapRepo: ValoMapRepo,
    private val valoSeasonRepo: ValoSeasonRepo
) {
    fun init() {
        valoVersionRepo.observeRemote()
            .combine(valoVersionRepo.observe()) { remoteVersion, localVersion ->
                if (remoteVersion.version != localVersion?.version) {
                    sync()
                    valoVersionRepo.upsert(remoteVersion)
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(scope)
    }

    private suspend fun sync() {
        withContext(Dispatchers.IO) {
            valoModeRepo.sync()
            valoMapRepo.sync()
            valoSeasonRepo.sync()
        }
    }

    fun stop() {
        scope.coroutineContext.cancelChildren()
    }
}
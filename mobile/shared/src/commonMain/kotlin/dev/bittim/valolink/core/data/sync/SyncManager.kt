/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SyncManager.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:39
 */

package dev.bittim.valolink.core.data.sync

import dev.bittim.valolink.core.domain.repo.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn

class SyncManager(
    private val scope: CoroutineScope,
    private val valoVersionRepo: ValoVersionRepo,
    private val valoModeRepo: ValoModeRepo,
    private val valoMapRepo: ValoMapRepo,
    private val valoSeasonRepo: ValoSeasonRepo,
    private val valoCompetitiveSeasonRepo: ValoCompetitiveSeasonRepo,
    private val valoRankTableRepo: ValoRankTableRepo,
    private val valoRankRepo: ValoRankRepo
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
            valoRankTableRepo.sync()
            valoCompetitiveSeasonRepo.sync()
            valoRankRepo.sync()
        }
    }

    fun stop() {
        scope.coroutineContext.cancelChildren()
    }
}
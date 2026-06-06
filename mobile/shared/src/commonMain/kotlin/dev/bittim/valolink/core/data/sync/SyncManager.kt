/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SyncManager.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 23:07
 */

package dev.bittim.valolink.core.data.sync

import dev.bittim.valolink.core.domain.repo.ValoVersionRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn

class SyncManager(
    private val scope: CoroutineScope,
    private val valoVersionRepo: ValoVersionRepo
) {
    fun init() {
        valoVersionRepo.observeRemote()
            .combine(valoVersionRepo.observe()) { remoteVersion, localVersion ->
                if (remoteVersion.version != localVersion?.version) {
                    valoVersionRepo.upsert(remoteVersion)
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(scope)
    }

    fun stop() {
        scope.coroutineContext.cancelChildren()
    }
}
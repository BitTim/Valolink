/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       VersionApiRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.repository.version

import androidx.room.withTransaction
import dev.bittim.valolink.content.data.local.ContentDatabase
import dev.bittim.valolink.content.data.remote.ContentApi
import dev.bittim.valolink.content.domain.model.Version
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VersionApiRepository @Inject constructor(
    private val contentDatabase: ContentDatabase,
    private val contentApi: ContentApi,
) : VersionRepository {
    override suspend fun get(): Flow<Version> {
        val response = contentApi.getVersion()

        return if (response.isSuccessful) {
            val version = response.body()!!.data!!.toEntity()
            contentDatabase.withTransaction {
                contentDatabase.versionDao.upsert(version)
            }

            flowOf(version.toType())
        } else {
            contentDatabase.versionDao.get().distinctUntilChanged()
                .map { it?.toType() ?: Version.EMPTY }
        }
    }
}
/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 03:44
 */

package dev.bittim.valolink.content.data.repository.contract

import dev.bittim.valolink.content.data.repository.ContentRepository
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.content.domain.model.contract.chapter.Level
import dev.bittim.valolink.content.domain.model.contract.content.ContentType
import kotlinx.coroutines.flow.Flow

interface ContractRepository : ContentRepository<Contract> {
    suspend fun getByUuid(uuid: String, withRewards: Boolean): Flow<Contract?>
    suspend fun getAll(withRewards: Boolean): Flow<List<Contract>>

    suspend fun getActiveContracts(withRewards: Boolean): Flow<List<Contract>>
    suspend fun getAgentGears(withRewards: Boolean): Flow<List<Contract>>
    suspend fun getInactiveContracts(
        contentType: ContentType,
        withRewards: Boolean
    ): Flow<List<Contract>>

    suspend fun getLevelByUuid(uuid: String): Flow<Level?>
    suspend fun getLevelByDependency(dependency: String): Flow<Level?>
}

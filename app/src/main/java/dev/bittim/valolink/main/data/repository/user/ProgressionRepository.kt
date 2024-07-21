package dev.bittim.valolink.main.data.repository.user

import dev.bittim.valolink.main.domain.model.user.Progression
import kotlinx.coroutines.flow.Flow

interface ProgressionRepository {
    suspend fun getCurrentProgressions(): Flow<List<Progression>>
    suspend fun getProgressionsByUser(uid: String): Flow<List<Progression>>
    suspend fun getCurrentProgression(contract: String): Flow<Progression?>
    suspend fun getProgression(
        uid: String,
        contract: String,
    ): Flow<Progression?>

    suspend fun setProgression(
        progression: Progression,
    ): Boolean
}
package dev.bittim.valolink.main.data.repository.user

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.user.UserDatabase
import dev.bittim.valolink.main.data.local.user.entity.ProgressionEntity
import dev.bittim.valolink.main.data.worker.user.ProgressionSyncWorker
import dev.bittim.valolink.main.domain.model.user.Progression
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProgressionSupabaseRepository @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userDatabase: UserDatabase,
    private val workManager: WorkManager,
) : ProgressionRepository {
    // ================================
    //  Get Gears
    // ================================

    override suspend fun getCurrentProgressions(): Flow<List<Progression>> {
        val uid = sessionRepository.getUid() ?: return flow { }
        return getProgressionsByUser(uid)
    }

    override suspend fun getProgressionsByUser(uid: String): Flow<List<Progression>> {
        return try {
            // Get from local database
            val localGears = userDatabase.gearDao
                .getByUser(uid)
                .distinctUntilChanged()
                .map { gears -> gears.map { it.toType() } }

            // Queue worker to sync with Supabase
            queueWorker(uid)

            // Return
            localGears
        } catch (e: Exception) {
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getCurrentProgression(contract: String): Flow<Progression?> {
        val uid = sessionRepository.getUid() ?: return flow { }
        return getProgression(
            uid,
            contract
        )
    }

    override suspend fun getProgression(
        uid: String,
        contract: String,
    ): Flow<Progression?> {
        return try {
            // Get from local database
            val localGear = userDatabase.gearDao.getByUserAndContract(
                uid,
                contract
            ).distinctUntilChanged().map { it?.toType() }

            // Queue worker to sync with Supabase
            queueWorker(uid)

            // Return
            localGear
        } catch (e: Exception) {
            e.printStackTrace()
            return flow { }
        }
    }

    // ================================
    //  Set Gears
    // ================================

    override suspend fun setProgression(
        progression: Progression,
    ): Boolean {
        return try {
            // Add to local database
            userDatabase.gearDao.upsert(ProgressionEntity.fromType(progression))

            // Queue worker to sync with Supabase
            queueWorker(progression.user)

            // Return
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ================================
    //  Private methods
    // ================================

    private fun queueWorker(uid: String) {
        val workRequest = OneTimeWorkRequestBuilder<ProgressionSyncWorker>()
            .setInputData(workDataOf(ProgressionSyncWorker.KEY_UID to uid))
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            "GearSyncWorker",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}
package dev.bittim.valolink.main.data.repository.user

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.user.UserDatabase
import dev.bittim.valolink.main.data.local.user.entity.GearEntity
import dev.bittim.valolink.main.data.worker.user.GearSyncWorker
import dev.bittim.valolink.main.domain.model.Gear
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GearSupabaseRepository @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userDatabase: UserDatabase,
    private val workManager: WorkManager,
) : GearRepository {
    // ================================
    //  Get Gears
    // ================================

    override suspend fun getCurrentGears(): Flow<List<Gear>> {
        val uid = sessionRepository.getUid() ?: return flow { }
        return getGears(uid)
    }

    override suspend fun getGears(uid: String): Flow<List<Gear>> {
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

    override suspend fun getCurrentGear(contract: String): Flow<Gear?> {
        val uid = sessionRepository.getUid() ?: return flow { }
        return getGear(
            uid,
            contract
        )
    }

    override suspend fun getGear(
        uid: String,
        contract: String,
    ): Flow<Gear?> {
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
    
    override suspend fun upsertGear(
        gear: Gear,
    ) {
        try {
            // Add to local database
            userDatabase.gearDao.upsert(GearEntity.fromType(gear))

            // Queue worker to sync with Supabase
            queueWorker(gear.user)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ================================
    //  Private methods
    // ================================

    private fun queueWorker(uid: String) {
        val workRequest = OneTimeWorkRequestBuilder<GearSyncWorker>()
            .setInputData(workDataOf(GearSyncWorker.KEY_UID to uid))
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            "GearSyncWorker",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}
package dev.bittim.valolink.main.data.repository.user.data

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.user.UserDatabase
import dev.bittim.valolink.main.data.local.user.entity.UserLevelEntity
import dev.bittim.valolink.main.data.remote.user.dto.UserLevelDto
import dev.bittim.valolink.main.data.worker.user.UserSyncWorker
import dev.bittim.valolink.main.domain.model.user.UserLevel
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class UserLevelSupabaseRepository @Inject constructor(
    private val userDatabase: UserDatabase,
    private val database: Postgrest,
    private val workManager: WorkManager,
) : UserLevelRepository {
    companion object {
        const val TABLE_NAME = "levels"
    }

    // ================================
    //  Get
    // ================================

    override suspend fun getAll(uid: String): Flow<List<UserLevel>> {
        return try {
            // Get from local database
            val levelsFlow = userDatabase.userLevelDao
                .getByContract(uid)
                .distinctUntilChanged()
                .map { levels -> levels.map { it.toType() } }

            // Queue worker to sync with Supabase
            queueWorker(uid)

            // Return
            levelsFlow
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun get(uid: String, uuid: String): Flow<UserLevel?> {
        return try {
            // Get from local database
            val levelFlow = userDatabase.userLevelDao.getByContractAndLevel(
                uid,
                uuid
            ).distinctUntilChanged().map { it?.toType() }

            // Queue worker to sync with Supabase
            queueWorker(uid)

            // Return
            levelFlow
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    // ================================
    //  Set
    // ================================

    override suspend fun set(obj: UserLevel, toDelete: Boolean): Boolean {
        return try {
            // Add to local database
            userDatabase.userLevelDao.upsert(
                UserLevelEntity.fromType(obj, false, toDelete)
            )

            // Queue worker to sync with Supabase
            queueWorker(obj.userContract)

            // Return
            true
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            false
        }
    }

    override suspend fun set(obj: UserLevel): Boolean {
        return set(obj, false)
    }

    override suspend fun delete(obj: UserLevel): Boolean {
        return set(obj, true)
    }

    // ================================
    //  Remote operations
    // ================================

    override suspend fun remoteQuery(relation: String): List<UserLevelDto>? {
        try {
            return database.from(TABLE_NAME).select {
                filter { UserLevelDto::userContract eq relation }
            }.decodeList<UserLevelDto>()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return null
        }
    }

    override suspend fun remoteUpsert(uuid: String): Boolean {
        return try {
            val entity = userDatabase.userLevelDao.getByUuid(uuid).firstOrNull() ?: return false
            val dto = UserLevelDto.fromEntity(entity)

            database.from(TABLE_NAME).upsert(dto)
            true
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            false
        }
    }

    override suspend fun remoteDelete(uuid: String): Boolean {
        return try {
            database.from(TABLE_NAME).delete {
                filter { UserLevelDto::uuid eq uuid }
            }
            true
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            false
        }
    }

    // ================================
    //  Worker
    // ================================

    override fun queueWorker(userContract: String) {
        val workRequest = OneTimeWorkRequestBuilder<UserSyncWorker>()
            .setInputData(
                workDataOf(
                    UserSyncWorker.KEY_TYPE to UserLevel::class.simpleName,
                    UserSyncWorker.KEY_RELATION to userContract
                )
            )
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()

        workManager.enqueueUniqueWork(
            UserLevel::class.simpleName + UserSyncWorker.WORK_BASE_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}
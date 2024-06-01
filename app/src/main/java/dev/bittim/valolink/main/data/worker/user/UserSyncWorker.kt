package dev.bittim.valolink.main.data.worker.user

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.main.data.local.user.UserDatabase
import dev.bittim.valolink.main.data.remote.user.dto.UserDataDto
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.take
import java.time.OffsetDateTime

@HiltWorker
class UserSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val database: Postgrest,
    private val userDatabase: UserDatabase,
) : CoroutineWorker(
    context,
    params
) {
    override suspend fun doWork(): Result {
        // Retrieve user id from input data
        val uid = inputData.getString(KEY_UID) ?: return Result.failure()

        // Get most recent local update timestamp
        val lastLocalUpdate = userDatabase.userDataDao.getUpdatedAtByUuid(uid).take(1).firstOrNull()
        Log.d(
            "UserSyncWorker",
            "Last local update: $lastLocalUpdate"
        )

        // Fetch most recent data from Supabase
        val user = database.from("users").select {
            filter {
                UserDataDto::uuid eq uid
            }
            limit(1)
            single()
        }.decodeAsOrNull<UserDataDto>()

        // Upsert Supabase data only, if it is more recent
        if (user != null && (lastLocalUpdate.isNullOrEmpty() || OffsetDateTime
                .parse(lastLocalUpdate)
                .isBefore(OffsetDateTime.parse(user.updatedAt)))
        ) {
            userDatabase.userDataDao.upsert(user.toEntity())
        }

        // Push write queue to Supabase
        userDatabase.userDataDao.getSyncQueue().take(1).collect {
            it.forEach { userData ->
                database.from("users").upsert(UserDataDto.fromEntity(userData))
                userDatabase.userDataDao.upsert(userData.copy(isSynced = true))
            }
        }

        return Result.success()
    }

    companion object {
        const val KEY_UID = "KEY_UID"
    }
}
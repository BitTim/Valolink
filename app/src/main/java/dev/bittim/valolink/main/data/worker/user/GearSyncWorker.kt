package dev.bittim.valolink.main.data.worker.user

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.main.data.local.user.UserDatabase
import dev.bittim.valolink.main.data.remote.user.dto.GearDto
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.take
import java.time.OffsetDateTime

@HiltWorker
class GearSyncWorker @AssistedInject constructor(
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
        val lastLocalUpdates = userDatabase.gearDao.getByUser(uid).take(1).firstOrNull()

        // Fetch most recent gear data from Supabase
        var gears: List<GearDto?> = listOf()
        try {
            gears = database.from("gears").select {
                filter {
                    GearDto::user eq uid
                }
            }.decodeList<GearDto>()
        } catch (e: Exception) {
            if (e !is RestException) {
                e.printStackTrace()
                return Result.retry()
            }
        }

        // Upsert Supabase data only, if it is more recent
        try {
            gears.forEach { gear ->
                if (gear != null && (lastLocalUpdates.isNullOrEmpty() || OffsetDateTime
                        .parse(lastLocalUpdates.find { it.uuid == gear.uuid }?.updatedAt)
                        .isBefore(OffsetDateTime.parse(gear.updatedAt)))
                ) {
                    userDatabase.gearDao.upsert(gear.toEntity())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Push write queue to Supabase
        userDatabase.gearDao.getSyncQueue().take(1).collect {
            it.forEach { gear ->
                if (gear == null) return@forEach

                database.from("gears").upsert(GearDto.fromEntity(gear))
                userDatabase.gearDao.upsert(gear.copy(isSynced = true))
            }
        }

        return Result.success()
    }



    companion object {
        const val KEY_UID = "KEY_UID"
    }
}
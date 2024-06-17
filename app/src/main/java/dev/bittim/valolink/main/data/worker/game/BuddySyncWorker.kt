package dev.bittim.valolink.main.data.worker.game

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.main.data.repository.game.BuddyRepository
import dev.bittim.valolink.main.data.repository.game.VersionRepository
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class BuddySyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val buddyRepository: BuddyRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Get input data from params
        val buddyUuid = params.inputData.getString(KEY_BUDDY_UUID)
        val version = params.inputData.getString(KEY_VERSION)
        if (version.isNullOrEmpty()) return Result.failure()

        // Fetch from API
        if (buddyUuid.isNullOrEmpty()) buddyRepository.fetchAll(version)
        else buddyRepository.fetch(buddyUuid, version)

        return Result.success()
    }


    companion object {
        const val KEY_VERSION = "KEY_VERSION"
        const val KEY_BUDDY_UUID = "KEY_BUDDY_UUID"
        const val WORK_NAME = "BuddySyncWorker"
    }
}
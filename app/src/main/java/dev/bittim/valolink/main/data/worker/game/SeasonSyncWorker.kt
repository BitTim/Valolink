package dev.bittim.valolink.main.data.worker.game

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.main.data.repository.game.SeasonRepository

@HiltWorker
class SeasonSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val seasonRepository: SeasonRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Get input data from params
        val seasonUuid = params.inputData.getString(KEY_SEASON_UUID)
        val version = params.inputData.getString(KEY_VERSION)
        if (version.isNullOrEmpty()) return Result.failure()

        // Fetch from API
        if (seasonUuid.isNullOrEmpty()) seasonRepository.fetchAll(version)
        else seasonRepository.fetch(seasonUuid, version)

        return Result.success()
    }


    companion object {
        const val KEY_VERSION = "KEY_VERSION"
        const val KEY_SEASON_UUID = "KEY_SEASON_UUID"
        const val WORK_NAME = "SeasonSyncWorker"
    }
}
package dev.bittim.valolink.main.data.worker.game

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.main.data.repository.game.PlayerTitleRepository

@HiltWorker
class PlayerTitleSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val playerTitleRepository: PlayerTitleRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Get input data from params
        val playerTitleUuid = params.inputData.getString(KEY_PLAYERTITLE_UUID)
        val version = params.inputData.getString(KEY_VERSION)
        if (version.isNullOrEmpty()) return Result.failure()

        // Fetch from API
        if (playerTitleUuid.isNullOrEmpty()) playerTitleRepository.fetchAll(version)
        else playerTitleRepository.fetch(playerTitleUuid, version)

        return Result.success()
    }


    companion object {
        const val KEY_VERSION = "KEY_VERSION"
        const val KEY_PLAYERTITLE_UUID = "KEY_PLAYERTITLE_UUID"
        const val WORK_NAME = "PlayerTitleSyncWorker"
    }
}
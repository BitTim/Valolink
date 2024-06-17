package dev.bittim.valolink.main.data.worker.game

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.main.data.repository.game.PlayerCardRepository

@HiltWorker
class PlayerCardSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val playerCardRepository: PlayerCardRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Get input data from params
        val playerCardUuid = params.inputData.getString(KEY_PLAYERCARD_UUID)
        val version = params.inputData.getString(KEY_VERSION)
        if (version.isNullOrEmpty()) return Result.failure()

        // Fetch from API
        if (playerCardUuid.isNullOrEmpty()) playerCardRepository.fetchAll(version)
        else playerCardRepository.fetch(playerCardUuid, version)

        return Result.success()
    }


    companion object {
        const val KEY_VERSION = "KEY_VERSION"
        const val KEY_PLAYERCARD_UUID = "KEY_PLAYERCARD_UUID"
        const val WORK_NAME = "PlayerCardSyncWorker"
    }
}
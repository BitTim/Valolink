package dev.bittim.valolink.main.data.worker.game

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.main.data.repository.game.SprayRepository

@HiltWorker
class SpraySyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val sprayRepository: SprayRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Get input data from params
        val sprayUuid = params.inputData.getString(KEY_SPRAY_UUID)
        val version = params.inputData.getString(KEY_VERSION)
        Log.d("SPRAY_WORKER", "Version: $version")
        if (version.isNullOrEmpty()) return Result.retry()

        // Fetch from API
        if (sprayUuid.isNullOrEmpty()) sprayRepository.fetchAll(version)
        else sprayRepository.fetch(sprayUuid, version)

        return Result.success()
    }


    companion object {
        const val KEY_VERSION = "KEY_VERSION"
        const val KEY_SPRAY_UUID = "KEY_SPRAY_UUID"
        const val WORK_NAME = "SpraySyncWorker"
    }
}
package dev.bittim.valolink.main.data.worker.game

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.main.data.repository.game.WeaponRepository

@HiltWorker
class WeaponSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val weaponRepository: WeaponRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Get input data from params
        val weaponUuid = params.inputData.getString(KEY_WEAPON_UUID)
        val version = params.inputData.getString(KEY_VERSION)
        if (version.isNullOrEmpty()) return Result.failure()

        // Fetch from API
        if (weaponUuid.isNullOrEmpty()) weaponRepository.fetchAll(version)
        else weaponRepository.fetch(weaponUuid, version)

        return Result.success()
    }


    companion object {
        const val KEY_VERSION = "KEY_VERSION"
        const val KEY_WEAPON_UUID = "KEY_WEAPON_UUID"
        const val WORK_NAME = "WeaponSyncWorker"
    }
}
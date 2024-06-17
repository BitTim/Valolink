package dev.bittim.valolink.main.data.worker.game

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.main.data.repository.game.CurrencyRepository

@HiltWorker
class CurrencySyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val currencyRepository: CurrencyRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Get input data from params
        val currencyUuid = params.inputData.getString(KEY_CURRENCY_UUID)
        val version = params.inputData.getString(KEY_VERSION)
        if (version.isNullOrEmpty()) return Result.failure()

        // Fetch from API
        if (currencyUuid.isNullOrEmpty()) currencyRepository.fetchAll(version)
        else currencyRepository.fetch(currencyUuid, version)

        return Result.success()
    }


    companion object {
        const val KEY_VERSION = "KEY_VERSION"
        const val KEY_CURRENCY_UUID = "KEY_CURRENCY_UUID"
        const val WORK_NAME = "CurrencySyncWorker"
    }
}
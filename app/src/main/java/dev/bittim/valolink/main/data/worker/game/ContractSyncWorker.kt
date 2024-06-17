package dev.bittim.valolink.main.data.worker.game

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.main.data.repository.game.ContractRepository

@HiltWorker
class ContractSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val contractRepository: ContractRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Get input data from params
        val contractUuid = params.inputData.getString(KEY_CONTRACT_UUID)
        val version = params.inputData.getString(KEY_VERSION)
        if (version.isNullOrEmpty()) return Result.failure()

        // Fetch from API
        if (contractUuid.isNullOrEmpty()) contractRepository.fetchAllContracts(version)
        else contractRepository.fetchContract(contractUuid, version)

        return Result.success()
    }


    companion object {
        const val KEY_VERSION = "KEY_VERSION"
        const val KEY_CONTRACT_UUID = "KEY_CONTRACT_UUID"
        const val WORK_NAME = "ContractSyncWorker"
    }
}
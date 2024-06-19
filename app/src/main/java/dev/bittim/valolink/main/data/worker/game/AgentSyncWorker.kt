package dev.bittim.valolink.main.data.worker.game

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.repository.game.AgentRepository
import dev.bittim.valolink.main.data.repository.game.VersionRepository
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class AgentSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val gameDatabase: GameDatabase,
    private val versionRepository: VersionRepository,
    private val agentRepository: AgentRepository,
) : CoroutineWorker(
    context,
    params
) {
    override suspend fun doWork(): Result {
        // Get input data from params
        val uuid = params.inputData.getString(KEY_UUID)

        // Get versions of local cache
        val localVersions = gameDatabase.agentDao.getAll().firstOrNull()?.map { it.agent.version }

        // Get remote version
        val remoteVersion = versionRepository.get().firstOrNull()?.version
        if (remoteVersion.isNullOrEmpty()) return Result.retry()

        // Fetch from API if remote version is different
        if (localVersions == null || localVersions.any { it != remoteVersion }) {
            if (uuid.isNullOrEmpty()) agentRepository.fetchAll(remoteVersion)
            else agentRepository.fetch(
                uuid,
                remoteVersion
            )
        }

        return Result.success()
    }


    companion object {
        const val KEY_UUID = "KEY_UUID"
        const val WORK_NAME = "AgentSyncWorker"
    }
}
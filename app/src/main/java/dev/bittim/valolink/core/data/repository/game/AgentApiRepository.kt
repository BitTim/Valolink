package dev.bittim.valolink.core.data.repository.game

import androidx.room.withTransaction
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.core.data.local.game.GameDatabase
import dev.bittim.valolink.core.data.remote.game.GameApi
import dev.bittim.valolink.core.data.worker.game.GameSyncWorker
import dev.bittim.valolink.main.domain.model.game.agent.Agent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AgentApiRepository @Inject constructor(
	private val gameDatabase: GameDatabase,
	private val gameApi: GameApi,
	private val workManager: WorkManager,
) : AgentRepository {
	// --------------------------------
	//  Query from Database
	// --------------------------------

	// -------- [ Single queries ] --------

	override suspend fun getByUuid(
		uuid: String,
	): Flow<Agent?> {
		return try {
			// Get from local database
			val local =
				gameDatabase.agentDao.getByUuid(uuid).distinctUntilChanged().map { it?.toType() }

			// Queue worker to fetch newest data from API
			//  -> Worker will check if fetch is needed itself
			queueWorker(uuid)

			// Return
			local
		} catch (e: Exception) {
			if (e is CancellationException) throw e
			e.printStackTrace()
			return flow { }
		}
	}

	// -------- [ Bulk queries ] --------

	override suspend fun getAll(): Flow<List<Agent>> {
		return try {
			// Get from local database
			val local = gameDatabase.agentDao.getAll().distinctUntilChanged()
				.map { entities -> entities.map { it.toType() } }

			// Queue worker to fetch newest data from API
			//  -> Worker will check if fetch is needed itself
			queueWorker()

			// Return
			local
		} catch (e: Exception) {
			if (e is CancellationException) throw e
			e.printStackTrace()
			return flow { }
		}
	}

	override suspend fun getAllBaseAgentUuids(): Flow<Set<String>> {
		return try {
			// Get from local database
			val local =
				gameDatabase.agentDao.getBase().distinctUntilChanged().map { it.keys.toSet() }

			// Queue worker to fetch newest data from API
			//  -> Worker will check if fetch is needed itself
			queueWorker()

			// Return
			local
		} catch (e: Exception) {
			if (e is CancellationException) throw e
			e.printStackTrace()
			return flow { }
		}
	}

	// --------------------------------
	//  Fetching from API
	// --------------------------------

	// -------- [ Single fetching ] --------

	override suspend fun fetch(
		uuid: String,
		version: String,
	) {
		val response = gameApi.getAgent(uuid)
		if (response.isSuccessful) {
			val agentDto = response.body()!!.data!!
			val agent = agentDto.toEntity(version)
			val recruitment = agentDto.recruitmentData?.toEntity(version)
			val role = agentDto.role.toEntity(version)
			val abilities = agentDto.abilities.map {
				it.toEntity(
					version, agent.uuid
				)
			}

			gameDatabase.withTransaction {
				if (recruitment != null) {
					gameDatabase.contractsDao.upsertRecruitment(
						recruitment
					)
				}

				gameDatabase.agentDao.upsert(
					role, agent, abilities.distinct().toSet()
				)
			}
		}
	}

	// -------- [ Bulk fetching ] --------

	override suspend fun fetchAll(version: String) {
		val response = gameApi.getAllAgents()
		if (response.isSuccessful) {
			val agentDto = response.body()!!.data!!
			val agents = agentDto.map { it.toEntity(version) }

			val recruitments = agentDto.mapNotNull { data ->
				data.recruitmentData?.toEntity(version)
			}

			val roles = agentDto.map {
				it.role.toEntity(version)
			}

			val abilities = agentDto.zip(agents) { data, agent ->
				data.abilities.map {
					it.toEntity(
						version, agent.uuid
					)
				}
			}.flatten()

			gameDatabase.withTransaction {
				gameDatabase.contractsDao.upsertRecruitment(
					recruitments.distinct().toSet()
				)

				gameDatabase.agentDao.upsert(
					roles.distinct().toSet(),
					agents.distinct().toSet(),
					abilities.distinct().toSet()
				)
			}
		}
	}

	// ================================
	//  Queue Worker
	// ================================

	override fun queueWorker(
		uuid: String?,
	) {
		val workRequest = OneTimeWorkRequestBuilder<GameSyncWorker>().setInputData(
			workDataOf(
				GameSyncWorker.KEY_TYPE to Agent::class.simpleName,
				GameSyncWorker.KEY_UUID to uuid,
			)
		).setConstraints(Constraints(NetworkType.CONNECTED)).build()
		workManager.enqueueUniqueWork(
			Agent::class.simpleName + GameSyncWorker.WORK_BASE_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
			ExistingWorkPolicy.KEEP,
			workRequest
		)
	}
}
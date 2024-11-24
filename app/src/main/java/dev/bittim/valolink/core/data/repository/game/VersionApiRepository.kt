package dev.bittim.valolink.core.data.repository.game

import androidx.room.withTransaction
import dev.bittim.valolink.core.data.local.game.GameDatabase
import dev.bittim.valolink.core.data.remote.game.GameApi
import dev.bittim.valolink.main.domain.model.game.Version
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VersionApiRepository @Inject constructor(
	private val gameDatabase: GameDatabase,
	private val gameApi: GameApi,
) : VersionRepository {
	override suspend fun get(): Flow<Version> {
		val response = gameApi.getVersion()

		return if (response.isSuccessful) {
			val version = response.body()!!.data!!.toEntity()
			gameDatabase.withTransaction {
				gameDatabase.versionDao.upsert(version)
			}

			flowOf(version.toType())
		} else {
			gameDatabase.versionDao.get().distinctUntilChanged()
				.map { it?.toType() ?: Version.EMPTY }
		}
	}
}
package dev.bittim.valolink.core.data.repository.content.version

import androidx.room.withTransaction
import dev.bittim.valolink.core.data.local.content.ContentDatabase
import dev.bittim.valolink.core.data.remote.content.ContentApi
import dev.bittim.valolink.main.domain.model.game.Version
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VersionApiRepository @Inject constructor(
	private val contentDatabase: ContentDatabase,
	private val contentApi: ContentApi,
) : VersionRepository {
	override suspend fun get(): Flow<Version> {
		val response = contentApi.getVersion()

		return if (response.isSuccessful) {
			val version = response.body()!!.data!!.toEntity()
			contentDatabase.withTransaction {
				contentDatabase.versionDao.upsert(version)
			}

			flowOf(version.toType())
		} else {
			contentDatabase.versionDao.get().distinctUntilChanged()
				.map { it?.toType() ?: Version.EMPTY }
		}
	}
}
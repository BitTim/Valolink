package dev.bittim.valolink.feature.main.data.repository.game

import dev.bittim.valolink.feature.main.data.remote.game.GameApi
import dev.bittim.valolink.feature.main.data.remote.game.dto.VersionDto
import javax.inject.Inject

class VersionApiRepository @Inject constructor(
    private val gameApi: GameApi
) : VersionRepository {
    override suspend fun getApiVersion(): VersionDto? {
        val versionResponse = try {
            gameApi.getVersion()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return if (versionResponse.isSuccessful && versionResponse.body() != null && versionResponse.body()!!.data != null) {
            versionResponse.body()!!.data!!
        } else {
            null
        }
    }
}
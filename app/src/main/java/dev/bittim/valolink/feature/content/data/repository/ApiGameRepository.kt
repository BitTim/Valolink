package dev.bittim.valolink.feature.content.data.repository

import androidx.room.withTransaction
import dev.bittim.valolink.feature.content.data.local.game.GameDatabase
import dev.bittim.valolink.feature.content.data.remote.game.GameApi
import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto
import dev.bittim.valolink.feature.content.domain.model.Season
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ApiGameRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi
) : GameRepository {
    override suspend fun getApiVersion(): VersionDto? {
        val versionResponse = try {
            gameApi.getVersion()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return if (versionResponse.isSuccessful &&
            versionResponse.body() != null &&
            versionResponse.body()!!.data != null
        ) {
            versionResponse.body()!!.data!!
        } else {
            null
        }
    }


    override suspend fun getAllSeasons(): Flow<List<Season>> {
        val version = getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAllSeasons().onEach { entities ->
            if (entities.isEmpty() || entities.any { it.version != version }) {
                val response = gameApi.getSeasons()
                if (response.isSuccessful) {
                    gameDatabase.withTransaction {
                        gameDatabase.dao.upsertAllSeasons(response.body()!!.data!!.map {
                            it.toEntity(version)
                        })
                    }
                }
            }
        }.map { seasons ->
            seasons.map { it.toType() }
        }
    }

    override suspend fun getAllContracts(): Flow<List<Contract>> {
        val version = getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAllContracts().onEach { entities ->
            if (entities.isEmpty() || entities.any { it.contract.version != version }) {
                val response = gameApi.getContracts()
                if (response.isSuccessful) {
                    val contractDto = response.body()!!.data!!
                    val contracts = contractDto.map { it.toEntity(version) }

                    val contents = contractDto.zip(contracts) { data, contract ->
                        data.content.toEntity(version, contract.uuid)
                    }

                    val chapterDto = contractDto.map { it.content.chapters }.flatten()
                    val chapters = contractDto.zip(contents) { data, content ->
                        data.content.chapters.map() {
                            it.toEntity(version, content.uuid)
                        }
                    }.flatten()

                    val levelDto = chapterDto.map { it.levels }.flatten()
                    val levels = chapterDto.zip(chapters) { data, chapter ->
                        data.levels.map {
                            it.toEntity(version, chapter.uuid)
                        }
                    }.flatten()

                    val rewards = levelDto.zip(levels) { data, level ->
                        data.reward.toEntity(version, levelUuid = level.uuid)
                    }.plus(
                        chapterDto.zip(chapters) { data, chapter ->
                            data.freeRewards?.map {
                                it.toEntity(version, chapterUuid = chapter.uuid)
                            }.orEmpty()
                        }.flatten()
                    )

                    gameDatabase.dao.upsertAllContracts(
                        contracts = contracts,
                        contents = contents,
                        chapters = chapters,
                        levels = levels,
                        rewards = rewards
                    )
                }
            }
        }.map { contracts ->
            contracts.map { it.toType() }
        }
    }
}
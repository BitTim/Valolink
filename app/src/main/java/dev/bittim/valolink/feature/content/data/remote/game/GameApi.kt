package dev.bittim.valolink.feature.content.data.remote.game

import dev.bittim.valolink.feature.content.data.remote.game.dto.MapDto
import dev.bittim.valolink.feature.content.data.remote.game.dto.SeasonDto
import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto
import retrofit2.Response
import retrofit2.http.GET

interface GameApi {
    @GET("version")
    suspend fun getVersion(): Response<GameApiResponse<VersionDto>>

    @GET("seasons")
    suspend fun getSeasons(): Response<GameApiResponse<List<SeasonDto>>>

    @GET("maps")
    suspend fun getMaps(): Response<GameApiResponse<List<MapDto>>>

    companion object {
        const val BASE_URL = "https://valorant-api.com/v1/"
    }
}
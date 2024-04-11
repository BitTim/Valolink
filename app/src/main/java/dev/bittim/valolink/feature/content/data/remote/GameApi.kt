package dev.bittim.valolink.feature.content.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface GameApi {
    @GET("version")
    suspend fun getVersion(): Response<GameApiResponse<VersionDto>>

    @GET("seasons")
    suspend fun getSeasons(): Response<GameApiResponse<List<SeasonDto>>>

    companion object {
        const val BASE_URL = "https://valorant-api.com/v1/"
    }
}
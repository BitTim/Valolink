package dev.bittim.valolink.feature.content.data.remote.game

import dev.bittim.valolink.feature.content.data.remote.game.dto.AgentDto
import dev.bittim.valolink.feature.content.data.remote.game.dto.ContractDto
import dev.bittim.valolink.feature.content.data.remote.game.dto.CurrencyDto
import dev.bittim.valolink.feature.content.data.remote.game.dto.EventDto
import dev.bittim.valolink.feature.content.data.remote.game.dto.SeasonDto
import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GameApi {
    @GET("version")
    suspend fun getVersion(): Response<GameApiResponse<VersionDto>>



    @GET("seasons/{uuid}")
    suspend fun getSeason(@Path("uuid") uuid: String): Response<GameApiResponse<SeasonDto>>

    @GET("seasons")
    suspend fun getAllSeasons(): Response<GameApiResponse<List<SeasonDto>>>



    @GET("events/{uuid}")
    suspend fun getEvent(@Path("uuid") uuid: String): Response<GameApiResponse<EventDto>>



    @GET("events")
    suspend fun getAllEvents(): Response<GameApiResponse<List<EventDto>>>

    @GET("contract/{uuid}")
    suspend fun getContract(@Path("uuid") uuid: String): Response<GameApiResponse<ContractDto>>

    @GET("contracts")
    suspend fun getAllContracts(): Response<GameApiResponse<List<ContractDto>>>



    @GET("agents/{uuid}")
    suspend fun getAgent(@Path("uuid") uuid: String): Response<GameApiResponse<AgentDto>>



    @GET("agents?isPlayableCharacter=true")
    suspend fun getAllAgents(): Response<GameApiResponse<List<AgentDto>>>



    @GET("currencies/{uuid}")
    suspend fun getCurrency(@Path("uuid") uuid: String): Response<GameApiResponse<CurrencyDto>>

    
    
    
    companion object {
        const val BASE_URL = "https://valorant-api.com/v1/"
    }
}
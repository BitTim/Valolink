package dev.bittim.valolink.main.data.remote.game

import dev.bittim.valolink.main.data.remote.game.dto.CurrencyDto
import dev.bittim.valolink.main.data.remote.game.dto.EventDto
import dev.bittim.valolink.main.data.remote.game.dto.PlayerCardDto
import dev.bittim.valolink.main.data.remote.game.dto.PlayerTitleDto
import dev.bittim.valolink.main.data.remote.game.dto.SeasonDto
import dev.bittim.valolink.main.data.remote.game.dto.SprayDto
import dev.bittim.valolink.main.data.remote.game.dto.VersionDto
import dev.bittim.valolink.main.data.remote.game.dto.agents.AgentDto
import dev.bittim.valolink.main.data.remote.game.dto.buddy.BuddyDto
import dev.bittim.valolink.main.data.remote.game.dto.contract.ContractDto
import dev.bittim.valolink.main.data.remote.game.dto.weapon.WeaponDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GameApi {
    // --------------------------------
    //  Version
    // --------------------------------

    @GET("version")
    suspend fun getVersion(): Response<GameApiResponse<VersionDto>>

    // --------------------------------
    //  Seasons
    // --------------------------------

    @GET("seasons/{uuid}")
    suspend fun getSeason(@Path("uuid") uuid: String): Response<GameApiResponse<SeasonDto>>

    @GET("seasons")
    suspend fun getAllSeasons(): Response<GameApiResponse<List<SeasonDto>>>

    // --------------------------------
    //  Events
    // --------------------------------

    @GET("events/{uuid}")
    suspend fun getEvent(@Path("uuid") uuid: String): Response<GameApiResponse<EventDto>>

    @GET("events")
    suspend fun getAllEvents(): Response<GameApiResponse<List<EventDto>>>

    // --------------------------------
    //  Contracts
    // --------------------------------

    @GET("contract/{uuid}")
    suspend fun getContract(@Path("uuid") uuid: String): Response<GameApiResponse<ContractDto>>

    @GET("contracts")
    suspend fun getAllContracts(): Response<GameApiResponse<List<ContractDto>>>

    // --------------------------------
    //  Agents
    // --------------------------------

    @GET("agents/{uuid}")
    suspend fun getAgent(@Path("uuid") uuid: String): Response<GameApiResponse<AgentDto>>

    @GET("agents?isPlayableCharacter=true")
    suspend fun getAllAgents(): Response<GameApiResponse<List<AgentDto>>>

    // --------------------------------
    //  Currencies
    // --------------------------------

    @GET("currencies/{uuid}")
    suspend fun getCurrency(@Path("uuid") uuid: String): Response<GameApiResponse<CurrencyDto>>

    // --------------------------------
    //  Sprays
    // --------------------------------

    @GET("sprays/{uuid}")
    suspend fun getSpray(@Path("uuid") uuid: String): Response<GameApiResponse<SprayDto>>

    // --------------------------------
    //  Weapons
    // --------------------------------

    @GET("weapons")
    suspend fun getAllWeapons(): Response<GameApiResponse<List<WeaponDto>>>

    @GET("weapons/{uuid}")
    suspend fun getWeapon(@Path("uuid") uuid: String): Response<GameApiResponse<WeaponDto>>

    // --------------------------------
    //  Player Cards
    // --------------------------------

    @GET("playercards/{uuid}")
    suspend fun getPlayerCard(@Path("uuid") uuid: String): Response<GameApiResponse<PlayerCardDto>>

    // --------------------------------
    //  Buddies
    // --------------------------------

    @GET("buddies")
    suspend fun getAllBuddies(): Response<GameApiResponse<List<BuddyDto>>>

    @GET("buddies/{uuid}")
    suspend fun getBuddy(@Path("uuid") uuid: String): Response<GameApiResponse<BuddyDto>>

    // --------------------------------
    //  Player Titles
    // --------------------------------

    @GET("playertitles/{uuid}")
    suspend fun getPlayerTitle(@Path("uuid") uuid: String): Response<GameApiResponse<PlayerTitleDto>>



    companion object {
        const val BASE_URL = "https://valorant-api.com/v1/"
    }
}
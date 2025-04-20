/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContentApi.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.content.data.remote

import dev.bittim.valolink.content.data.remote.dto.CurrencyDto
import dev.bittim.valolink.content.data.remote.dto.EventDto
import dev.bittim.valolink.content.data.remote.dto.FlexDto
import dev.bittim.valolink.content.data.remote.dto.PlayerCardDto
import dev.bittim.valolink.content.data.remote.dto.PlayerTitleDto
import dev.bittim.valolink.content.data.remote.dto.SeasonDto
import dev.bittim.valolink.content.data.remote.dto.SprayDto
import dev.bittim.valolink.content.data.remote.dto.VersionDto
import dev.bittim.valolink.content.data.remote.dto.agents.AgentDto
import dev.bittim.valolink.content.data.remote.dto.buddy.BuddyDto
import dev.bittim.valolink.content.data.remote.dto.contract.ContractDto
import dev.bittim.valolink.content.data.remote.dto.rank.RankTableDto
import dev.bittim.valolink.content.data.remote.dto.weapon.WeaponDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ContentApi {
    companion object {
        const val BASE_URL = "https://valorant-api.com/v1/"
    }

    // --------------------------------
    //  Version
    // --------------------------------

    @GET("version")
    suspend fun getVersion(): Response<ContentApiResponse<VersionDto>>

    // --------------------------------
    //  Seasons
    // --------------------------------

    @GET("seasons/{uuid}")
    suspend fun getSeason(@Path("uuid") uuid: String): Response<ContentApiResponse<SeasonDto>>

    @GET("seasons")
    suspend fun getAllSeasons(): Response<ContentApiResponse<List<SeasonDto>>>

    // --------------------------------
    //  Events
    // --------------------------------

    @GET("events/{uuid}")
    suspend fun getEvent(@Path("uuid") uuid: String): Response<ContentApiResponse<EventDto>>

    @GET("events")
    suspend fun getAllEvents(): Response<ContentApiResponse<List<EventDto>>>

    // --------------------------------
    //  Contracts
    // --------------------------------

    @GET("contract/{uuid}")
    suspend fun getContract(@Path("uuid") uuid: String): Response<ContentApiResponse<ContractDto>>

    @GET("contracts")
    suspend fun getAllContracts(): Response<ContentApiResponse<List<ContractDto>>>

    // --------------------------------
    //  Agents
    // --------------------------------

    @GET("agents/{uuid}")
    suspend fun getAgent(@Path("uuid") uuid: String): Response<ContentApiResponse<AgentDto>>

    @GET("agents?isPlayableCharacter=true")
    suspend fun getAllAgents(): Response<ContentApiResponse<List<AgentDto>>>

    // --------------------------------
    //  Currencies
    // --------------------------------

    @GET("currencies/{uuid}")
    suspend fun getCurrency(@Path("uuid") uuid: String): Response<ContentApiResponse<CurrencyDto>>

    @GET("currencies")
    suspend fun getAllCurrencies(): Response<ContentApiResponse<List<CurrencyDto>>>

    // --------------------------------
    //  Sprays
    // --------------------------------

    @GET("sprays/{uuid}")
    suspend fun getSpray(@Path("uuid") uuid: String): Response<ContentApiResponse<SprayDto>>

    @GET("sprays")
    suspend fun getAllSprays(): Response<ContentApiResponse<List<SprayDto>>>

    // --------------------------------
    //  Weapons
    // --------------------------------

    @GET("weapons")
    suspend fun getAllWeapons(): Response<ContentApiResponse<List<WeaponDto>>>

    @GET("weapons/{uuid}")
    suspend fun getWeapon(@Path("uuid") uuid: String): Response<ContentApiResponse<WeaponDto>>

    // --------------------------------
    //  Player Cards
    // --------------------------------

    @GET("playercards/{uuid}")
    suspend fun getPlayerCard(@Path("uuid") uuid: String): Response<ContentApiResponse<PlayerCardDto>>

    @GET("playercards")
    suspend fun getAllPlayerCards(): Response<ContentApiResponse<List<PlayerCardDto>>>

    // --------------------------------
    //  Buddies
    // --------------------------------

    @GET("buddies")
    suspend fun getAllBuddies(): Response<ContentApiResponse<List<BuddyDto>>>

    @GET("buddies/{uuid}")
    suspend fun getBuddy(@Path("uuid") uuid: String): Response<ContentApiResponse<BuddyDto>>

    // --------------------------------
    //  Player Titles
    // --------------------------------

    @GET("playertitles/{uuid}")
    suspend fun getPlayerTitle(@Path("uuid") uuid: String): Response<ContentApiResponse<PlayerTitleDto>>

    @GET("playertitles")
    suspend fun getAllPlayerTitles(): Response<ContentApiResponse<List<PlayerTitleDto>>>

    // --------------------------------
    //  Flexes
    // --------------------------------

    @GET("flex/{uuid}")
    suspend fun getFlex(@Path("uuid") uuid: String): Response<ContentApiResponse<FlexDto>>

    @GET("flex")
    suspend fun getAllFlexes(): Response<ContentApiResponse<List<FlexDto>>>

    // --------------------------------
    //  Ranks
    // --------------------------------

    @GET("competitivetiers/{uuid}")
    suspend fun getRankTable(@Path("uuid") uuid: String): Response<ContentApiResponse<RankTableDto>>

    @GET("competitivetiers")
    suspend fun getAllRankTables(): Response<ContentApiResponse<List<RankTableDto>>>
}

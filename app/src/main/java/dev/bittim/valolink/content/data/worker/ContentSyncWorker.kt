/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ContentSyncWorker.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.content.data.local.ContentDatabase
import dev.bittim.valolink.content.data.repository.agent.AgentRepository
import dev.bittim.valolink.content.data.repository.buddy.BuddyRepository
import dev.bittim.valolink.content.data.repository.contract.ContractRepository
import dev.bittim.valolink.content.data.repository.currency.CurrencyRepository
import dev.bittim.valolink.content.data.repository.event.EventRepository
import dev.bittim.valolink.content.data.repository.playerCard.PlayerCardRepository
import dev.bittim.valolink.content.data.repository.playerTitle.PlayerTitleRepository
import dev.bittim.valolink.content.data.repository.season.SeasonRepository
import dev.bittim.valolink.content.data.repository.spray.SprayRepository
import dev.bittim.valolink.content.data.repository.version.VersionRepository
import dev.bittim.valolink.content.data.repository.weapon.WeaponRepository
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class ContentSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val contentDatabase: ContentDatabase,
    private val versionRepository: VersionRepository,
    private val agentRepository: AgentRepository,
    private val buddyRepository: BuddyRepository,
    private val contractRepository: ContractRepository,
    private val currencyRepository: CurrencyRepository,
    private val eventRepository: EventRepository,
    private val playerCardRepository: PlayerCardRepository,
    private val playerTitleRepository: PlayerTitleRepository,
    private val seasonRepository: SeasonRepository,
    private val sprayRepository: SprayRepository,
    private val weaponRepository: WeaponRepository,
) : CoroutineWorker(
    context, params
) {
    override suspend fun doWork(): Result {
        // Get input data from params
        val type = inputData.getString(KEY_TYPE)
        val uuid = inputData.getString(KEY_UUID)

        // Get repository that shall be used
        val repository = when (type) {
            "Agent" -> agentRepository
            "Buddy" -> buddyRepository
            "Contract" -> contractRepository
            "Currency" -> currencyRepository
            "Event" -> eventRepository
            "PlayerCard" -> playerCardRepository
            "PlayerTitle" -> playerTitleRepository
            "Season" -> seasonRepository
            "Spray" -> sprayRepository
            "Weapon" -> weaponRepository

            else -> return Result.failure()
        }

        // Get versions of local cache
        val localVersions =
            contentDatabase.getAllOfType(type).firstOrNull()?.map { it?.version }

        // Get remote version
        val remoteVersion = versionRepository.get().firstOrNull()?.version
        if (remoteVersion.isNullOrEmpty()) return Result.retry()

        // Fetch from API if remote version is different
        if (localVersions.isNullOrEmpty() || localVersions.any { it != remoteVersion }) {
            if (uuid.isNullOrEmpty()) repository.fetchAll(remoteVersion)
            else repository.fetch(uuid, remoteVersion)
        }

        return Result.success()
    }


    companion object {
        const val KEY_TYPE = "KEY_TYPE"
        const val KEY_UUID = "KEY_UUID"
        const val WORK_BASE_NAME = "GameSyncWorker"
    }
}
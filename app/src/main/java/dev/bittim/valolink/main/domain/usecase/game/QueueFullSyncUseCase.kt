package dev.bittim.valolink.main.domain.usecase.game

import dev.bittim.valolink.main.data.repository.game.AgentRepository
import dev.bittim.valolink.main.data.repository.game.BuddyRepository
import dev.bittim.valolink.main.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.game.CurrencyRepository
import dev.bittim.valolink.main.data.repository.game.EventRepository
import dev.bittim.valolink.main.data.repository.game.PlayerCardRepository
import dev.bittim.valolink.main.data.repository.game.PlayerTitleRepository
import dev.bittim.valolink.main.data.repository.game.SeasonRepository
import dev.bittim.valolink.main.data.repository.game.SprayRepository
import dev.bittim.valolink.main.data.repository.game.VersionRepository
import dev.bittim.valolink.main.data.repository.game.WeaponRepository
import kotlinx.coroutines.flow.firstOrNull

class QueueFullSyncUseCase(
    private val agentRepository: AgentRepository,
    private val buddyRepository: BuddyRepository,
    private val contractRepository: ContractRepository,
    private val currencyRepository: CurrencyRepository,
    private val eventRepository: EventRepository,
    private val playerCardRepository: PlayerCardRepository,
    private val playerTitleRepository: PlayerTitleRepository,
    private val seasonRepository: SeasonRepository,
    private val sprayRepository: SprayRepository,
    private val versionRepository: VersionRepository,
    private val weaponRepository: WeaponRepository
) {
    suspend operator fun invoke() {
        val version = versionRepository.get().firstOrNull()?.version
        if (version.isNullOrEmpty()) return

        // TODO: Optimize to check if cached data is still fresh before fetching
        agentRepository.queueWorker(version)
        buddyRepository.queueWorker(version)
        contractRepository.queueWorker(version)
        currencyRepository.queueWorker(version)
        eventRepository.queueWorker(version)
        playerCardRepository.queueWorker(version)
        playerTitleRepository.queueWorker(version)
        seasonRepository.queueWorker(version)
        sprayRepository.queueWorker(version)
        weaponRepository.queueWorker(version)
    }
}
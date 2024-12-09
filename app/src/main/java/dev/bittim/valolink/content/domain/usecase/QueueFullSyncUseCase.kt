package dev.bittim.valolink.content.domain.usecase

import dev.bittim.valolink.content.data.repository.agent.AgentRepository
import dev.bittim.valolink.content.data.repository.buddy.BuddyRepository
import dev.bittim.valolink.content.data.repository.contract.ContractRepository
import dev.bittim.valolink.content.data.repository.currency.CurrencyRepository
import dev.bittim.valolink.content.data.repository.event.EventRepository
import dev.bittim.valolink.content.data.repository.playerCard.PlayerCardRepository
import dev.bittim.valolink.content.data.repository.playerTitle.PlayerTitleRepository
import dev.bittim.valolink.content.data.repository.season.SeasonRepository
import dev.bittim.valolink.content.data.repository.spray.SprayRepository
import dev.bittim.valolink.content.data.repository.weapon.WeaponRepository

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
    private val weaponRepository: WeaponRepository,
) {
    operator fun invoke() {
        agentRepository.queueWorker()
        buddyRepository.queueWorker()
        contractRepository.queueWorker()
        currencyRepository.queueWorker()
        eventRepository.queueWorker()
        playerCardRepository.queueWorker()
        playerTitleRepository.queueWorker()
        seasonRepository.queueWorker()
        sprayRepository.queueWorker()
        weaponRepository.queueWorker()
    }
}
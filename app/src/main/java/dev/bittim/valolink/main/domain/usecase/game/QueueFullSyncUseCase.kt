package dev.bittim.valolink.main.domain.usecase.game

import dev.bittim.valolink.core.data.repository.content.agent.AgentRepository
import dev.bittim.valolink.core.data.repository.content.buddy.BuddyRepository
import dev.bittim.valolink.core.data.repository.content.contract.ContractRepository
import dev.bittim.valolink.core.data.repository.content.currency.CurrencyRepository
import dev.bittim.valolink.core.data.repository.content.event.EventRepository
import dev.bittim.valolink.core.data.repository.content.playerCard.PlayerCardRepository
import dev.bittim.valolink.core.data.repository.content.playerTitle.PlayerTitleRepository
import dev.bittim.valolink.core.data.repository.content.season.SeasonRepository
import dev.bittim.valolink.core.data.repository.content.spray.SprayRepository
import dev.bittim.valolink.core.data.repository.content.weapon.WeaponRepository

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
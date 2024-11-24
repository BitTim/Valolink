package dev.bittim.valolink.main.domain.usecase.game

import dev.bittim.valolink.core.data.repository.game.AgentRepository
import dev.bittim.valolink.core.data.repository.game.BuddyRepository
import dev.bittim.valolink.core.data.repository.game.ContractRepository
import dev.bittim.valolink.core.data.repository.game.CurrencyRepository
import dev.bittim.valolink.core.data.repository.game.EventRepository
import dev.bittim.valolink.core.data.repository.game.PlayerCardRepository
import dev.bittim.valolink.core.data.repository.game.PlayerTitleRepository
import dev.bittim.valolink.core.data.repository.game.SeasonRepository
import dev.bittim.valolink.core.data.repository.game.SprayRepository
import dev.bittim.valolink.core.data.repository.game.WeaponRepository

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
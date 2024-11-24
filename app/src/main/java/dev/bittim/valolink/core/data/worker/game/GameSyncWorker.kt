package dev.bittim.valolink.core.data.worker.game

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.bittim.valolink.core.data.local.game.GameDatabase
import dev.bittim.valolink.core.data.repository.game.AgentRepository
import dev.bittim.valolink.core.data.repository.game.BuddyRepository
import dev.bittim.valolink.core.data.repository.game.ContractRepository
import dev.bittim.valolink.core.data.repository.game.CurrencyRepository
import dev.bittim.valolink.core.data.repository.game.EventRepository
import dev.bittim.valolink.core.data.repository.game.PlayerCardRepository
import dev.bittim.valolink.core.data.repository.game.PlayerTitleRepository
import dev.bittim.valolink.core.data.repository.game.SeasonRepository
import dev.bittim.valolink.core.data.repository.game.SprayRepository
import dev.bittim.valolink.core.data.repository.game.VersionRepository
import dev.bittim.valolink.core.data.repository.game.WeaponRepository
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class GameSyncWorker @AssistedInject constructor(
	@Assisted private val context: Context,
	@Assisted private val params: WorkerParameters,
	private val gameDatabase: GameDatabase,
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
			"Agent"       -> agentRepository
			"Buddy"       -> buddyRepository
			"Contract"    -> contractRepository
			"Currency"    -> currencyRepository
			"Event"       -> eventRepository
			"PlayerCard"  -> playerCardRepository
			"PlayerTitle" -> playerTitleRepository
			"Season"      -> seasonRepository
			"Spray"       -> sprayRepository
			"Weapon"      -> weaponRepository

			else          -> return Result.failure()
		}

		// Get versions of local cache
		val localVersions =
			gameDatabase.getAllOfType(type).firstOrNull()?.map { it?.getApiVersion() }

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
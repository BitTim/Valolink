package dev.bittim.valolink.main.domain.usecase.user

import dev.bittim.valolink.main.data.repository.user.ProgressionRepository
import dev.bittim.valolink.main.domain.model.user.Progression
import java.util.UUID
import javax.inject.Inject

class AddUserGearUseCase @Inject constructor(
    private val progressionRepository: ProgressionRepository,
) {
    suspend operator fun invoke(
        uid: String,
        contract: String,
    ) {
        val gear = Progression(
            UUID.randomUUID().toString(),
            uid,
            contract,
            0
        )

        progressionRepository.setProgression(gear)
    }
}
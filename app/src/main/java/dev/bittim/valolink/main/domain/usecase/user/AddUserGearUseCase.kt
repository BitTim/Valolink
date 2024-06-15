package dev.bittim.valolink.main.domain.usecase.user

import dev.bittim.valolink.main.data.repository.user.GearRepository
import dev.bittim.valolink.main.domain.model.user.Gear
import java.util.UUID
import javax.inject.Inject

class AddUserGearUseCase @Inject constructor(
    private val gearRepository: GearRepository,
) {
    suspend operator fun invoke(
        uid: String,
        contract: String,
    ) {
        val gear = Gear(
            UUID.randomUUID().toString(),
            uid,
            contract,
            0
        )

        gearRepository.setGear(gear)
    }
}
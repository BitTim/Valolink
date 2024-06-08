package dev.bittim.valolink.main.data.repository.user

import dev.bittim.valolink.main.domain.model.Gear
import kotlinx.coroutines.flow.Flow

interface GearRepository {
    suspend fun getCurrentGears(): Flow<List<Gear>>
    suspend fun getGears(uid: String): Flow<List<Gear>>
    suspend fun getCurrentGear(contract: String): Flow<Gear?>
    suspend fun getGear(
        uid: String,
        contract: String,
    ): Flow<Gear?>

    suspend fun upsertGear(
        gear: Gear,
    )
}
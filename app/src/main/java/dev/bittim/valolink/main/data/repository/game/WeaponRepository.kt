package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.weapon.Weapon
import dev.bittim.valolink.main.domain.model.game.weapon.skins.WeaponSkin
import kotlinx.coroutines.flow.Flow

interface WeaponRepository {
    suspend fun getSkinByLevelUuid(
        levelUuid: String,
        providedVersion: String? = null,
    ): Flow<WeaponSkin>

    suspend fun getAll(
        providedVersion: String? = null,
    ): Flow<List<Weapon>>

    suspend fun fetch(
        uuid: String,
        version: String,
    )

    suspend fun fetchAll(version: String)
    fun queueWorker(version: String, uuid: String? = null)
}
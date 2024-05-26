package dev.bittim.valolink.feature.main.data.repository.game

import dev.bittim.valolink.feature.main.domain.model.game.weapon.WeaponSkinLevel
import kotlinx.coroutines.flow.Flow

interface WeaponSkinLevelRepository {
    suspend fun getWeaponSkinLevel(
        uuid: String,
        providedVersion: String? = null
    ): Flow<WeaponSkinLevel>

    suspend fun fetchWeaponSkinLevel(uuid: String, version: String)
}
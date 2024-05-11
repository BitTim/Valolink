package dev.bittim.valolink.feature.content.data.repository.game

import dev.bittim.valolink.feature.content.domain.model.weapon.WeaponSkinLevel
import kotlinx.coroutines.flow.Flow

interface WeaponSkinLevelRepository {
    suspend fun getWeaponSkinLevel(
        uuid: String,
        providedVersion: String? = null
    ): Flow<WeaponSkinLevel>

    suspend fun fetchWeaponSkinLevel(uuid: String, version: String)
}
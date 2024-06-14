package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.weapon.skins.WeaponSkinLevel
import kotlinx.coroutines.flow.Flow

interface WeaponSkinLevelRepository {
    suspend fun getWeaponSkinLevel(
        uuid: String,
        providedVersion: String? = null,
    ): Flow<WeaponSkinLevel>

    suspend fun fetchWeaponSkinLevel(
        uuid: String,
        version: String,
    )
}
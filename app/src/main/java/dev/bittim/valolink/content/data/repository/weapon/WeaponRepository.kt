package dev.bittim.valolink.content.data.repository.weapon

import dev.bittim.valolink.content.data.repository.ContentRepository
import dev.bittim.valolink.content.domain.model.weapon.Weapon
import dev.bittim.valolink.content.domain.model.weapon.skins.WeaponSkin
import kotlinx.coroutines.flow.Flow

interface WeaponRepository : ContentRepository<Weapon> {
    suspend fun getSkinByLevelUuid(
        levelUuid: String,
    ): Flow<WeaponSkin?>
}
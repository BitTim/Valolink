package dev.bittim.valolink.core.data.repository.content.weapon

import dev.bittim.valolink.core.data.repository.content.ContentRepository
import dev.bittim.valolink.main.domain.model.game.weapon.Weapon
import dev.bittim.valolink.main.domain.model.game.weapon.skins.WeaponSkin
import kotlinx.coroutines.flow.Flow

interface WeaponRepository : ContentRepository<Weapon> {
    suspend fun getSkinByLevelUuid(
        levelUuid: String,
    ): Flow<WeaponSkin?>
}
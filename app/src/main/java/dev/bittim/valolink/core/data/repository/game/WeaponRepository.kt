package dev.bittim.valolink.core.data.repository.game

import dev.bittim.valolink.main.domain.model.game.weapon.Weapon
import dev.bittim.valolink.main.domain.model.game.weapon.skins.WeaponSkin
import kotlinx.coroutines.flow.Flow

interface WeaponRepository : GameRepository<Weapon> {
    suspend fun getSkinByLevelUuid(
        levelUuid: String,
    ): Flow<WeaponSkin?>
}
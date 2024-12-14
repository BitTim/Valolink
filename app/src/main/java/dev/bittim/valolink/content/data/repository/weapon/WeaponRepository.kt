/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

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
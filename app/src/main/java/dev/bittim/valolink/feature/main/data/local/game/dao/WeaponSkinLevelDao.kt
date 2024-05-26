package dev.bittim.valolink.feature.main.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.feature.main.data.local.game.entity.weapon.WeaponSkinLevelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeaponSkinLevelDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsertWeaponSkinLevel(weaponSkinLevel: WeaponSkinLevelEntity)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM WeaponSkinLevels WHERE uuid = :uuid LIMIT 1")
    fun getWeaponSkinLevel(uuid: String): Flow<WeaponSkinLevelEntity?>
}
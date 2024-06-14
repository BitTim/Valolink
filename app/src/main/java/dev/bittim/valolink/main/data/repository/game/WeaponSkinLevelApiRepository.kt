package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.domain.model.game.weapon.skins.WeaponSkinLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class WeaponSkinLevelApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
) : WeaponSkinLevelRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getWeaponSkinLevel(
        uuid: String,
        providedVersion: String?,
    ): Flow<WeaponSkinLevel> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.weaponSkinLevelDao
            .getWeaponSkinLevel(uuid)
            .distinctUntilChanged()
            .transform { entity ->
                if (entity == null || entity.version != version) {
                    fetchWeaponSkinLevel(
                        uuid,
                        version
                    )
                } else {
                    emit(entity)
                }
            }
            .map { it.toType() }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetchWeaponSkinLevel(
        uuid: String,
        version: String,
    ) {
        val response = gameApi.getWeaponSkinLevel(uuid)
        if (response.isSuccessful) {
            gameDatabase.weaponSkinLevelDao.upsertWeaponSkinLevel(
                response.body()!!.data!!.toEntity(
                    version
                )
            )
        }
    }
}
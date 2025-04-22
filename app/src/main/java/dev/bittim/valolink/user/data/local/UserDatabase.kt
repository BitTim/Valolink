/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserDatabase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   23.04.25, 00:35
 */

package dev.bittim.valolink.user.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.bittim.valolink.user.data.local.dao.UserAgentDao
import dev.bittim.valolink.user.data.local.dao.UserContractDao
import dev.bittim.valolink.user.data.local.dao.UserDataDao
import dev.bittim.valolink.user.data.local.dao.UserLevelDao
import dev.bittim.valolink.user.data.local.dao.UserRankDao
import dev.bittim.valolink.user.data.local.entity.SyncedEntity
import dev.bittim.valolink.user.data.local.entity.UserAgentEntity
import dev.bittim.valolink.user.data.local.entity.UserContractEntity
import dev.bittim.valolink.user.data.local.entity.UserDataEntity
import dev.bittim.valolink.user.data.local.entity.UserLevelEntity
import dev.bittim.valolink.user.data.local.entity.UserRankEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Database(
    entities = [UserDataEntity::class, UserAgentEntity::class, UserContractEntity::class, UserLevelEntity::class, UserRankEntity::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {
    companion object {
        const val LOCAL_UUID = "00000000-0000-0000-0000-000000000000"
    }

    abstract val userDataDao: UserDataDao
    abstract val userAgentDao: UserAgentDao
    abstract val userContractDao: UserContractDao
    abstract val userLevelDao: UserLevelDao
    abstract val userRankDao: UserRankDao

    fun getByRelation(type: String?, relation: String): Flow<List<SyncedEntity>> {
        return when (type) {
            "UserData" -> userDataDao.getByUuid(relation).map { listOfNotNull(it) }
            "UserAgent" -> userAgentDao.getByUser(relation)
            "UserContract" -> userContractDao.getByUser(relation)
            "UserLevel" -> userLevelDao.getByContract(relation)
            "UserRank" -> userRankDao.getByUuid(relation).map { listOfNotNull(it) }

            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }

    fun getSyncQueue(type: String?): Flow<List<SyncedEntity?>> {
        return when (type) {
            "UserData" -> userDataDao.getSyncQueue()
            "UserAgent" -> userAgentDao.getSyncQueue()
            "UserContract" -> userContractDao.getSyncQueue()
            "UserLevel" -> userLevelDao.getSyncQueue()
            "UserRank" -> userRankDao.getSyncQueue()

            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }

    suspend fun upsert(type: String?, entity: SyncedEntity) {
        when (type) {
            "UserData" -> userDataDao.upsert(entity as UserDataEntity)
            "UserAgent" -> userAgentDao.upsert(entity as UserAgentEntity)
            "UserContract" -> userContractDao.upsert(entity as UserContractEntity)
            "UserLevel" -> userLevelDao.upsert(entity as UserLevelEntity)
            "UserRank" -> userRankDao.upsert(entity as UserRankEntity)

            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }

    suspend fun deleteByUuid(type: String?, uuid: String) {
        when (type) {
            "UserData" -> userDataDao.deleteByUuid(uuid)
            "UserAgent" -> userAgentDao.deleteByUuid(uuid)
            "UserContract" -> userContractDao.deleteByUuid(uuid)
            "UserLevel" -> userLevelDao.deleteByUuid(uuid)
            "UserRank" -> userRankDao.deleteByUuid(uuid)

            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }
}

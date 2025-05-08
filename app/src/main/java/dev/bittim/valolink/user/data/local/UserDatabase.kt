/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserDatabase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.05.25, 13:40
 */

package dev.bittim.valolink.user.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.bittim.valolink.user.data.local.dao.UserAgentDao
import dev.bittim.valolink.user.data.local.dao.UserContractDao
import dev.bittim.valolink.user.data.local.dao.UserLevelDao
import dev.bittim.valolink.user.data.local.dao.UserMetaDao
import dev.bittim.valolink.user.data.local.dao.UserRankDao
import dev.bittim.valolink.user.data.local.entity.SyncedEntity
import dev.bittim.valolink.user.data.local.entity.UserAgentEntity
import dev.bittim.valolink.user.data.local.entity.UserContractEntity
import dev.bittim.valolink.user.data.local.entity.UserLevelEntity
import dev.bittim.valolink.user.data.local.entity.UserMetaEntity
import dev.bittim.valolink.user.data.local.entity.UserRankEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Database(
    entities = [UserMetaEntity::class, UserAgentEntity::class, UserContractEntity::class, UserLevelEntity::class, UserRankEntity::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {
    companion object {
        const val LOCAL_UUID = "00000000-0000-0000-0000-000000000000"
    }

    abstract val userMetaDao: UserMetaDao
    abstract val userAgentDao: UserAgentDao
    abstract val userContractDao: UserContractDao
    abstract val userLevelDao: UserLevelDao
    abstract val userRankDao: UserRankDao

    fun getByRelation(type: String?, relation: String): Flow<List<SyncedEntity>> {
        return when (type) {
            "UserMeta" -> userMetaDao.getByUuid(relation).map { listOfNotNull(it) }
            "UserAgent" -> userAgentDao.getByUser(relation)
            "UserContract" -> userContractDao.getByUser(relation)
            "UserLevel" -> userLevelDao.getByContract(relation)
            "UserRank" -> userRankDao.getByUuid(relation).map { listOfNotNull(it) }

            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }

    fun getSyncQueue(type: String?): Flow<List<SyncedEntity?>> {
        return when (type) {
            "UserMeta" -> userMetaDao.getSyncQueue()
            "UserAgent" -> userAgentDao.getSyncQueue()
            "UserContract" -> userContractDao.getSyncQueue()
            "UserLevel" -> userLevelDao.getSyncQueue()
            "UserRank" -> userRankDao.getSyncQueue()

            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }

    fun getDeleteQueue(type: String?): Flow<List<SyncedEntity?>> {
        return when (type) {
            "UserMeta" -> userMetaDao.getDeleteQueue()
            "UserAgent" -> userAgentDao.getDeleteQueue()
            "UserContract" -> userContractDao.getDeleteQueue()
            "UserLevel" -> userLevelDao.getDeleteQueue()
            "UserRank" -> userRankDao.getDeleteQueue()

            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }

    suspend fun upsert(type: String?, entity: SyncedEntity) {
        when (type) {
            "UserMeta" -> userMetaDao.upsert(entity as UserMetaEntity)
            "UserAgent" -> userAgentDao.upsert(entity as UserAgentEntity)
            "UserContract" -> userContractDao.upsert(entity as UserContractEntity)
            "UserLevel" -> userLevelDao.upsert(entity as UserLevelEntity)
            "UserRank" -> userRankDao.upsert(entity as UserRankEntity)

            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }

    suspend fun deleteByUuid(type: String?, uuid: String) {
        when (type) {
            "UserMeta" -> userMetaDao.deleteByUuid(uuid)
            "UserAgent" -> userAgentDao.deleteByUuid(uuid)
            "UserContract" -> userContractDao.deleteByUuid(uuid)
            "UserLevel" -> userLevelDao.deleteByUuid(uuid)
            "UserRank" -> userRankDao.deleteByUuid(uuid)

            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }
}

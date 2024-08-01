package dev.bittim.valolink.main.data.local.user

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.bittim.valolink.main.data.local.user.dao.UserAgentDao
import dev.bittim.valolink.main.data.local.user.dao.UserContractDao
import dev.bittim.valolink.main.data.local.user.dao.UserDataDao
import dev.bittim.valolink.main.data.local.user.dao.UserLevelDao
import dev.bittim.valolink.main.data.local.user.entity.SyncedEntity
import dev.bittim.valolink.main.data.local.user.entity.UserAgentEntity
import dev.bittim.valolink.main.data.local.user.entity.UserContractEntity
import dev.bittim.valolink.main.data.local.user.entity.UserDataEntity
import dev.bittim.valolink.main.data.local.user.entity.UserLevelEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Database(
    entities = [UserDataEntity::class, UserAgentEntity::class, UserContractEntity::class, UserLevelEntity::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDataDao: UserDataDao
    abstract val userAgentDao: UserAgentDao
    abstract val userContractDao: UserContractDao
    abstract val userLevelDao: UserLevelDao

    fun getByRelation(type: String?, relation: String): Flow<List<SyncedEntity>> {
        return when (type) {
            "UserData"     -> userDataDao.getByUuid(relation).map { listOfNotNull(it) }
            "UserAgent"    -> userAgentDao.getByUser(relation)
            "UserContract" -> userContractDao.getByUser(relation)
            "UserLevel"    -> userLevelDao.getByContract(relation)

            else           -> throw IllegalArgumentException("Unknown type: $type")
        }
    }

    fun getSyncQueue(type: String?): Flow<List<SyncedEntity?>> {
        return when (type) {
            "UserData"     -> userDataDao.getSyncQueue()
            "UserAgent"    -> userAgentDao.getSyncQueue()
            "UserContract" -> userContractDao.getSyncQueue()
            "UserLevel"    -> userLevelDao.getSyncQueue()

            else           -> throw IllegalArgumentException("Unknown type: $type")
        }
    }

    suspend fun upsert(type: String?, entity: SyncedEntity) {
        when (type) {
            "UserData"     -> userDataDao.upsert(entity as UserDataEntity)
            "UserAgent"    -> userAgentDao.upsert(entity as UserAgentEntity)
            "UserContract" -> userContractDao.upsert(entity as UserContractEntity)
            "UserLevel"    -> userLevelDao.upsert(entity as UserLevelEntity)

            else           -> throw IllegalArgumentException("Unknown type: $type")
        }
    }

    suspend fun deleteByUuid(type: String?, uuid: String) {
        when (type) {
            "UserData"     -> userDataDao.deleteByUuid(uuid)
            "UserAgent"    -> userAgentDao.deleteByUuid(uuid)
            "UserContract" -> userContractDao.deleteByUuid(uuid)
            "UserLevel"    -> userLevelDao.deleteByUuid(uuid)

            else           -> throw IllegalArgumentException("Unknown type: $type")
        }
    }
}
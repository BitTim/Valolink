package dev.bittim.valolink.main.data.local.user

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.bittim.valolink.main.data.local.converter.StringListConverter
import dev.bittim.valolink.main.data.local.user.dao.UserDataDao
import dev.bittim.valolink.main.data.local.user.entity.UserDataEntity

@Database(
    entities = [UserDataEntity::class],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDataDao: UserDataDao
}
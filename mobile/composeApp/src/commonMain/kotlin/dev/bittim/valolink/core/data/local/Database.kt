/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       Database.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 02:41
 */

package dev.bittim.valolink.core.data.local

import androidx.room.ConstructedBy
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import dev.bittim.valolink.core.data.local.converter.*
import dev.bittim.valolink.core.data.local.entity.*

@androidx.room.Database(
    version = 1,
    entities = [
        UserEntity::class,
        ActivityEntity::class,
        AgentEntity::class,
        FlagEntity::class,
        FollowEntity::class,
        MatchEntity::class,
        MatchParticipantEntity::class,
        ProgressionEntity::class,
        PurchasedLevelEntity::class,


    ]
)
@ConstructedBy(UserDatabaseConstructor::class)
@TypeConverters(
    UuidConverter::class,
    ActivityTypeConverter::class,
    AgentOwnedStateConverter::class,
    MatchEndReasonConverter::class,
    StringMapConverter::class
)
abstract class Database : RoomDatabase() {
}

@Suppress("KotlinNoActualForExpect")
expect object UserDatabaseConstructor: RoomDatabaseConstructor<Database> {
    override fun initialize(): Database
}
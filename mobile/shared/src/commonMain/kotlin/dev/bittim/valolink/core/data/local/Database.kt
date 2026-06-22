/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       Database.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 05:15
 */

package dev.bittim.valolink.core.data.local

import androidx.room.ConstructedBy
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import dev.bittim.valolink.core.data.local.converter.*
import dev.bittim.valolink.core.data.local.dao.ValoMapDao
import dev.bittim.valolink.core.data.local.dao.ValoModeDao
import dev.bittim.valolink.core.data.local.dao.ValoSeasonDao
import dev.bittim.valolink.core.data.local.dao.ValoVersionDao
import dev.bittim.valolink.core.data.local.entity.*

@androidx.room.Database(
    version = 1,
    entities = [
        ValoAgentAbilityEntity::class,
        ValoAgentEntity::class,
        ValoAgentRecruitmentEntity::class,
        ValoAgentRoleEntity::class,
        ValoBuddyEntity::class,
        ValoCardEntity::class,
        ValoCompetitiveSeasonBorderEntity::class,
        ValoCompetitiveSeasonEntity::class,
        ValoContentTierEntity::class,
        ValoCurrencyEntity::class,
        ValoEventEntity::class,
        ValoFlexEntity::class,
        ValoMapEntity::class,
        ValoModeEntity::class,
        ValoProgressionEntity::class,
        ValoProgressionLevelEntity::class,
        ValoProgressionLevelRewardEntity::class,
        ValoRankEntity::class,
        ValoRankTableEntity::class,
        ValoSeasonEntity::class,
        ValoSprayEntity::class,
        ValoThemeEntity::class,
        ValoTitleEntity::class,
        ValoVersionEntity::class,
        ValoWeaponEntity::class,
        ValoWeaponShopDataEntity::class,
        ValoWeaponSkinChromaEntity::class,
        ValoWeaponSkinEntity::class,
        ValoWeaponSkinLevelEntity::class,
        ValoWeaponStatsEntity::class
    ]
)
@ConstructedBy(DatabaseConstructor::class)
@TypeConverters(
    UuidConverter::class,
    InstantConverter::class,
    ActivityTypeConverter::class,
    AgentOwnedStateConverter::class,
    MatchEndReasonConverter::class,
    StringListConverter::class,
    StringMapConverter::class,
    GridPositionConverter::class,
    ValoMapCalloutConverter::class,
    ValoWeaponStatsAdsStatsConverter::class,
    ValoWeaponStatsAirBurstStatsConverter::class,
    ValoWeaponStatsAltShotgunStatsConverter::class,
    ValoWeaponStatsDamageRangesConverter::class
)
abstract class Database : RoomDatabase() {
    abstract fun valoVersionDao(): ValoVersionDao
    abstract fun valoModeDao(): ValoModeDao
    abstract fun valoMapDao(): ValoMapDao
    abstract fun valoSeasonDao(): ValoSeasonDao
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "KotlinNoActualForExpect")
expect object DatabaseConstructor: RoomDatabaseConstructor<Database> {
    override fun initialize(): Database
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DatabaseBuilder {
    fun get(): RoomDatabase.Builder<Database>
}
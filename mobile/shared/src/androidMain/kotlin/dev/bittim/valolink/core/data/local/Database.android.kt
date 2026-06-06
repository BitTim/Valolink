/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       Database.android.kt
 * Module:     Valolink.shared.androidMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 23:19
 */

package dev.bittim.valolink.core.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseBuilder(
    private val context: Context
) {
    actual fun get(): RoomDatabase.Builder<Database> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath("valolink.db")

        return Room.databaseBuilder<Database>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}
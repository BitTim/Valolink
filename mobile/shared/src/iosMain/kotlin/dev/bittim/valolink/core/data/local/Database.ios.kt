/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       Database.ios.kt
 * Module:     Valolink.shared.iosMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 23:19
 */

package dev.bittim.valolink.core.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseBuilder {
    actual fun get(): RoomDatabase.Builder<Database> {
        val dbFilePath = docDir() + "/valolink.db"
        return Room.databaseBuilder(name = dbFilePath)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun docDir(): String {
    val docDir = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )

    return requireNotNull(docDir?.path)
}
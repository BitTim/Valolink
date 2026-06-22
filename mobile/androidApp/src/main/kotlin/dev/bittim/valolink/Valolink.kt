/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       Valolink.kt
 * Module:     Valolink.androidApp.main
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 15:49
 */

package dev.bittim.valolink

import android.app.Application
import dev.bittim.valolink.core.di.initKoin
import org.koin.android.ext.koin.androidContext

class Valolink: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@Valolink)
        }
    }
}
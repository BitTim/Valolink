/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       InitKoin.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 17:55
 */

package dev.bittim.valolink.core.di

import dev.bittim.valolink.feature.activity.di.featureActivityModule
import dev.bittim.valolink.feature.auth.di.featureAuthModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
            platformModule,
            featureAuthModule,
            featureActivityModule
        )
    }
}
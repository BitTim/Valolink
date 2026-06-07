/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 17:44
 */

package dev.bittim.valolink.feature.activity.di

import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureActivityModule = module {
    viewModelOf(::ActivityAddFlowViewModel)
}
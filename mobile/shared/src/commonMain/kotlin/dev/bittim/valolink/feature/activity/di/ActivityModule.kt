/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   12.06.26, 04:10
 */

package dev.bittim.valolink.feature.activity.di

import dev.bittim.valolink.feature.activity.domain.usecase.FormatScoreUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.MatchOutcomeFromScoreUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.ParseIntUseCase
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureActivityModule = module {
    singleOf(::ParseIntUseCase)
    singleOf(::FormatScoreUseCase)
    singleOf(::MatchOutcomeFromScoreUseCase)

    viewModelOf(::ActivityAddFlowViewModel)
}
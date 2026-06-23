/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:30
 */

package dev.bittim.valolink.feature.activity.di

import dev.bittim.valolink.feature.activity.domain.usecase.*
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowViewModel
import dev.bittim.valolink.feature.activity.ui.screen.list.ActivityListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureActivityModule = module {
    singleOf(::ParseIntUseCase)
    singleOf(::FormatScoreUseCase)
    singleOf(::MatchOutcomeFromScoreUseCase)
    singleOf(::GetCurrentSeasonActivitiesForCurrentUserUseCase)
    singleOf(::CalculateRrUseCase)
    singleOf(::MapRrToRank)

    viewModelOf(::ActivityAddFlowViewModel)
    viewModelOf(::ActivityListViewModel)
}
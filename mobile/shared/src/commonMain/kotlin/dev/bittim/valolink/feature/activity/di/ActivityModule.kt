/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.06.26, 01:39
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
    singleOf(::GetSeasonActivitiesForCurrentUserByTimeUseCase)
    singleOf(::GetCurrentSeasonActivitiesForCurrentUserUseCase)
    singleOf(::CalculateRrBeforeTimeUseCase)
    singleOf(::CalculateRrUpToIdUseCase)
    singleOf(::MapRrToRank)
    singleOf(::ObserveRanksByTimeUseCase)

    viewModelOf(::ActivityAddFlowViewModel)
    viewModelOf(::ActivityListViewModel)
}
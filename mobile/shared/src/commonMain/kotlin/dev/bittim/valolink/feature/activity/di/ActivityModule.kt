/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 17:43
 */

package dev.bittim.valolink.feature.activity.di

import dev.bittim.valolink.feature.activity.domain.usecase.*
import dev.bittim.valolink.feature.activity.domain.usecase.rank.*
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowViewModel
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.state.ActivityAddFlowUiStateCalculator
import dev.bittim.valolink.feature.activity.ui.screen.list.ActivityListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureActivityModule = module {
    singleOf(::FilterRanksUseCase)

    singleOf(::ParseIntUseCase)
    singleOf(::FormatScoreUseCase)
    singleOf(::MatchOutcomeFromScoreUseCase)
    singleOf(::GetSeasonActivitiesForCurrentUserByTimeUseCase)
    singleOf(::CalculateRrBeforeTimeUseCase)
    singleOf(::CalculateRrUpToIdUseCase)
    singleOf(::CalculateTotalRrFromPlacementRankUseCase)
    singleOf(::MapRrToRankUseCase)
    singleOf(::ObserveRanksByTimeUseCase)
    singleOf(::CalculateRrDeltaUseCase)
    singleOf(::CalculateRankChangeUseCase)
    singleOf(::ActivityAddFlowUiStateCalculator)
    singleOf(::FinalizeActivityWithCurrentUserUseCase)

    viewModelOf(::ActivityAddFlowViewModel)
    viewModelOf(::ActivityListViewModel)
}

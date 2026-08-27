/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 19:58
 */

package dev.bittim.valolink.feature.activity.di

import dev.bittim.valolink.feature.activity.domain.usecase.FinalizeActivityWithCurrentUserUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.GetSeasonActivitiesForCurrentUserByTimeUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.ObserveRanksByTimeUseCase
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowViewModel
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.state.ActivityAddFlowUiStateCalculator
import dev.bittim.valolink.feature.activity.ui.screen.list.ActivityListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureActivityModule = module {
    singleOf(::GetSeasonActivitiesForCurrentUserByTimeUseCase)
    singleOf(::ObserveRanksByTimeUseCase)
    singleOf(::ActivityAddFlowUiStateCalculator)
    singleOf(::FinalizeActivityWithCurrentUserUseCase)

    viewModelOf(::ActivityAddFlowViewModel)
    viewModelOf(::ActivityListViewModel)
}

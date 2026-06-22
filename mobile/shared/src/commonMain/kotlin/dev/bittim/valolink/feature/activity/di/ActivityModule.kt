/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 16:35
 */

package dev.bittim.valolink.feature.activity.di

import dev.bittim.valolink.feature.activity.domain.usecase.FormatScoreUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.GetCurrentSeasonActivitiesForCurrentUserUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.MatchOutcomeFromScoreUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.ParseIntUseCase
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

    viewModelOf(::ActivityAddFlowViewModel)
    viewModelOf(::ActivityListViewModel)
}
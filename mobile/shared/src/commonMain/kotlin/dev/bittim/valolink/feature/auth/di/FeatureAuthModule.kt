/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FeatureAuthModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.05.26, 17:15
 */

package dev.bittim.valolink.feature.auth.di

import dev.bittim.valolink.feature.auth.domain.usecase.ValidateEmailUseCase
import dev.bittim.valolink.feature.auth.domain.usecase.ValidateOtpUseCase
import dev.bittim.valolink.feature.auth.ui.screen.AuthFlowViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureAuthModule = module {
    singleOf(::ValidateEmailUseCase)
    singleOf(::ValidateOtpUseCase)

    viewModelOf(::AuthFlowViewModel)
}
/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FeatureAuthModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 02:04
 */

package dev.bittim.valolink.feature.auth.di

import dev.bittim.valolink.feature.auth.domain.repo.AuthFlowRepo
import dev.bittim.valolink.feature.auth.domain.usecase.ValidateEmailUseCase
import dev.bittim.valolink.feature.auth.domain.usecase.ValidateOtpUseCase
import dev.bittim.valolink.feature.auth.ui.screen.email.EmailScreenViewModel
import dev.bittim.valolink.feature.auth.ui.screen.otp.OtpScreenViewModel
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val featureAuthModule = module {
    singleOf(::ValidateEmailUseCase)
    singleOf(::ValidateOtpUseCase)

    scope(named("AuthFlowScope")) {
        scopedOf(::AuthFlowRepo)

        viewModelOf(::EmailScreenViewModel)
        viewModelOf(::OtpScreenViewModel)
    }
}
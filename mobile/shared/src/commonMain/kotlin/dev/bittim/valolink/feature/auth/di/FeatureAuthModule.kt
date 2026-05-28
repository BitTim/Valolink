/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FeatureAuthModule.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   28.05.26, 21:14
 */

package dev.bittim.valolink.feature.auth.di

import dev.bittim.valolink.feature.auth.domain.usecase.ValidateEmailUseCase
import dev.bittim.valolink.feature.auth.domain.usecase.ValidatePasswordUseCase
import dev.bittim.valolink.feature.auth.ui.screen.email.EmailScreenViewModel
import dev.bittim.valolink.feature.auth.ui.screen.password.PasswordScreenViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureAuthModule = module {
    singleOf(::ValidateEmailUseCase)
    singleOf(::ValidatePasswordUseCase)

    viewModelOf(::EmailScreenViewModel)
    viewModelOf(::PasswordScreenViewModel)
}
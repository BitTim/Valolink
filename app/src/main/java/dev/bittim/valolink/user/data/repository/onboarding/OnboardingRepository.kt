/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       OnboardingRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.user.data.repository.onboarding

import dev.bittim.valolink.user.domain.model.UserAgent
import dev.bittim.valolink.user.domain.model.UserContract

interface OnboardingRepository {
    suspend fun setOnboardingComplete(
        agents: List<UserAgent>,
        userContracts: List<UserContract>,
    ): Boolean
}
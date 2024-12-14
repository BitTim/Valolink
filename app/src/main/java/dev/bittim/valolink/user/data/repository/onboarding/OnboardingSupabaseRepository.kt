/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       OnboardingSupabaseRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.user.data.repository.onboarding

import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.data.UserDataRepository
import dev.bittim.valolink.user.domain.model.UserAgent
import dev.bittim.valolink.user.domain.model.UserContract
import dev.bittim.valolink.user.domain.model.UserData
import kotlinx.serialization.json.put
import javax.inject.Inject

class OnboardingSupabaseRepository @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userDataRepository: UserDataRepository,
) : OnboardingRepository {
    override suspend fun setOnboardingComplete(
        agents: List<UserAgent>,
        userContracts: List<UserContract>,
    ): Boolean {
        sessionRepository.updateUserInfo {
            data {
                put(
                    "hasOnboarded", true
                )
            }
        }

        val uid = sessionRepository.getUid() ?: return false
        val username = sessionRepository.getUsernameFromMetadata() ?: return false

        return userDataRepository.setWithCurrentUser(
            UserData(
                uid, isPrivate = false, username, agents, userContracts
            )
        )
    }
}
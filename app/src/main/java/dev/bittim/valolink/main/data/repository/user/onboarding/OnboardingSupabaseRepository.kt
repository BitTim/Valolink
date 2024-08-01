package dev.bittim.valolink.main.data.repository.user.onboarding

import dev.bittim.valolink.main.data.repository.user.SessionRepository
import dev.bittim.valolink.main.data.repository.user.data.UserDataRepository
import dev.bittim.valolink.main.domain.model.user.UserAgent
import dev.bittim.valolink.main.domain.model.user.UserContract
import dev.bittim.valolink.main.domain.model.user.UserData
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
                    "hasOnboarded",
                    true
                )
            }
        }

        val uid = sessionRepository.getUid() ?: return false
        val username = sessionRepository.getUsernameFromMetadata() ?: return false

        return userDataRepository.setWithCurrentUser(
            UserData(
                uid,
                isPrivate = false,
                username,
                agents,
                userContracts
            )
        )
    }
}
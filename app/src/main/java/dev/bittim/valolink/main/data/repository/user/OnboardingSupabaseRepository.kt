package dev.bittim.valolink.main.data.repository.user

import dev.bittim.valolink.main.domain.model.user.Progression
import dev.bittim.valolink.main.domain.model.user.UserData
import kotlinx.serialization.json.put
import javax.inject.Inject

class OnboardingSupabaseRepository @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
) : OnboardingRepository {
    override suspend fun setOnboardingComplete(
        ownedAgentUuids: List<String>,
        progressions: List<Progression>,
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

        return userRepository.setCurrentUserData(
            UserData(
                uid,
                isPrivate = false,
                username,
                ownedAgentUuids,
                progressions
            )
        )
    }
}
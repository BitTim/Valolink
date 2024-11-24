package dev.bittim.valolink.main.ui.screens.onboarding.getstarted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.core.data.repository.game.AgentRepository
import dev.bittim.valolink.core.data.repository.game.ContractRepository
import dev.bittim.valolink.main.data.repository.user.SessionRepository
import dev.bittim.valolink.main.data.repository.user.onboarding.OnboardingRepository
import dev.bittim.valolink.main.domain.model.user.UserAgent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GetStartedViewModel @Inject constructor(
	private val sessionRepository: SessionRepository,
	private val onboardingRepository: OnboardingRepository,
	private val agentRepository: AgentRepository,
	private val contractRepository: ContractRepository,
) : ViewModel() {
	private val _state = MutableStateFlow(GetStartedState())
	val state = _state.asStateFlow()

	init {
		viewModelScope.launch {
			agentRepository.getAllBaseAgentUuids().collectLatest { uuids ->
				val uid = sessionRepository.getUid() ?: return@collectLatest

				_state.update {
					it.copy(
						userAgents = uuids.map { uuid ->
							UserAgent(
								UUID.randomUUID().toString(), uid, uuid
							)
						}, loadingFinished = state.value.loadingFinished or 0b0001
					)
				}
			}
		}

		viewModelScope.launch {
			contractRepository.getAgentGears().collectLatest { gears ->
				val uid = sessionRepository.getUid() ?: return@collectLatest

				_state.update {
					it.copy(
						userContracts = gears.map { gear -> gear.toUserObj(uid) },
						loadingFinished = state.value.loadingFinished or 0b0010
					)
				}
			}
		}
	}

	fun onGetStartedClicked(onNavToContent: () -> Unit) {
		viewModelScope.launch {
			if (state.value.loadingFinished != 0b0011) return@launch
			val userAgents = state.value.userAgents
			val userGears = state.value.userContracts

			val isSuccessful = onboardingRepository.setOnboardingComplete(
				userAgents, userGears
			)
			if (isSuccessful) onNavToContent()
		}
	}
}
package dev.bittim.valolink.feature.content.ui.screens.onboarding.getstarted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.feature.content.data.repository.UserRepository
import dev.bittim.valolink.feature.content.data.repository.game.AgentRepository
import dev.bittim.valolink.feature.content.data.repository.game.ContractRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetStartedViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val agentRepository: AgentRepository,
    private val contractRepository: ContractRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(GetStartedState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            agentRepository.getAllBaseAgentUuids()
                .collectLatest { uuids ->
                    _state.update {
                        it.copy(
                            ownedAgentUuids = uuids,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onGetStartedClicked(onNavToContent: () -> Unit) {
        viewModelScope.launch {
            if (state.value.isLoading) return@launch
            val ownedAgentUuids = state.value.ownedAgentUuids

            val isSuccessful = userRepository.setOnboardingComplete(ownedAgentUuids)
            if (isSuccessful) onNavToContent()
        }
    }
}
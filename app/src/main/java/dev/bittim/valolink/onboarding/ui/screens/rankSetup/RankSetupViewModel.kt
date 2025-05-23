/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankSetupViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   23.04.25, 00:34
 */

package dev.bittim.valolink.onboarding.ui.screens.rankSetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.data.repository.rank.RankRepository
import dev.bittim.valolink.onboarding.ui.screens.OnboardingScreen
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.data.UserMetaRepository
import dev.bittim.valolink.user.domain.model.UserRank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RankSetupViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userMetaRepository: UserMetaRepository,
    private val rankRepository: RankRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(RankSetupState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                rankRepository.getAllByLatestTable()
                    .stateIn(viewModelScope, WhileSubscribed(5000), null)
                    .collectLatest { ranks ->
                        _state.value = _state.value.copy(ranks = ranks?.dropLast(1))
                    }
            }
        }

        viewModelScope.launch {
            val userRank =
                userMetaRepository.getWithCurrentUser().firstOrNull()?.rank ?: return@launch

            _state.update {
                it.copy(
                    tier = userRank.tier,
                    rr = userRank.rr,
                    matchesPlayed = userRank.matchesPlayed,
                    matchesNeeded = userRank.matchesNeeded
                )
            }
        }
    }

    fun navBack() {
        viewModelScope.launch {
            val userData = userMetaRepository.getWithCurrentUser().firstOrNull() ?: return@launch
            userMetaRepository.setWithCurrentUser(
                userData.copy(
                    onboardingStep = OnboardingScreen.RankSetup.step - OnboardingScreen.STEP_OFFSET - 1
                )
            )
        }
    }

    fun onTierChanged(value: Int) {
        _state.update {
            it.copy(tier = value)
        }
    }

    fun onRRChanged(value: Int) {
        _state.update {
            it.copy(rr = value)
        }
    }

    fun onMatchesPlayedChanged(value: Int) {
        _state.update {
            it.copy(matchesPlayed = value)
        }
    }

    fun onMatchesNeededChanged(value: Int) {
        if (_state.value.matchesPlayed > value) {
            _state.update { it.copy(matchesPlayed = value, matchesNeeded = value) }
        } else {
            _state.update { it.copy(matchesNeeded = value) }
        }
    }

    fun setRank(tier: Int, rr: Int, matchesPlayed: Int, matchesNeeded: Int) {
        viewModelScope.launch {
            val uid = sessionRepository.getUid().firstOrNull() ?: return@launch
            val userData = userMetaRepository.get(uid).firstOrNull() ?: return@launch

            val actualPlayed = if (tier > 0) matchesNeeded else matchesPlayed

            userMetaRepository.setWithCurrentUser(
                userData.copy(
                    rank = UserRank(
                        uuid = uid,
                        tier = tier,
                        rr = rr,
                        matchesPlayed = actualPlayed,
                        matchesNeeded = matchesNeeded
                    ),
                    onboardingStep = OnboardingScreen.RankSetup.step - OnboardingScreen.STEP_OFFSET + 1
                )
            )
        }
    }
}

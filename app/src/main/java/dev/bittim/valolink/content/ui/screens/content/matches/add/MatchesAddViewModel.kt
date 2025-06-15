/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MatchesAddViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   15.06.25, 16:00
 */

package dev.bittim.valolink.content.ui.screens.content.matches.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.content.data.repository.map.MapRepository
import dev.bittim.valolink.content.data.repository.mode.ModeRepository
import dev.bittim.valolink.content.data.repository.rank.RankRepository
import dev.bittim.valolink.core.domain.model.ScoreResult
import dev.bittim.valolink.core.domain.usecase.score.DetermineScoreResultUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MatchesAddViewModel @Inject constructor(
    private val mapRepository: MapRepository,
    private val modeRepository: ModeRepository,
    private val rankRepository: RankRepository,
    private val determineScoreResultUseCase: DetermineScoreResultUseCase,
) : ViewModel() {
    private var _state = MutableStateFlow(MatchesAddState())
    val state = _state.asStateFlow()

    private var mapsFetchJob: Job? = null
    private var modesFetchJob: Job? = null
    private var ranksFetchJob: Job? = null

    init {
        mapsFetchJob?.cancel()
        mapsFetchJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mapRepository.getAll()
                    .stateIn(viewModelScope, WhileSubscribed(5000), null)
                    .collectLatest { maps ->
                        _state.update { it.copy(maps = maps?.sortedBy { map -> map.displayName }) }
                    }
            }
        }

        modesFetchJob?.cancel()
        modesFetchJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                modeRepository.getAll()
                    .stateIn(viewModelScope, WhileSubscribed(5000), null)
                    .collectLatest { modes ->
                        _state.update { it.copy(modes = modes) }
                    }
            }
        }

        ranksFetchJob?.cancel()
        ranksFetchJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                rankRepository.getAll()
                    .stateIn(viewModelScope, WhileSubscribed(5000), null)
                    .collectLatest { ranks ->
                        _state.update { it.copy(ranks = ranks) }
                    }
            }
        }
    }

    fun determineScoreResult(
        score: Int,
        enemyScore: Int?,
        surrender: Boolean,
        enemySurrender: Boolean
    ): ScoreResult {
        return determineScoreResultUseCase(score, enemyScore, surrender, enemySurrender)
    }
}
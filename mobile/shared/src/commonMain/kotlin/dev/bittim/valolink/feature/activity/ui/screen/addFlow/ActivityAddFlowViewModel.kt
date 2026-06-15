/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   15.06.26, 20:37
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bittim.valolink.core.data.util.fallbackLocale
import dev.bittim.valolink.core.domain.Result
import dev.bittim.valolink.core.domain.extension.toLocalizedString
import dev.bittim.valolink.core.domain.model.*
import dev.bittim.valolink.core.domain.repo.ValoMapRepo
import dev.bittim.valolink.core.domain.repo.ValoModeRepo
import dev.bittim.valolink.feature.activity.domain.usecase.FormatScoreUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.MatchOutcomeFromScoreUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.ParseIntUseCase
import dev.bittim.valolink.feature.activity.ui.components.map.MapCardState
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCardState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import valolink.shared.generated.resources.*
import kotlin.uuid.Uuid

class ActivityAddFlowViewModel(
    private val parseIntUseCase: ParseIntUseCase,
    private val formatScoreUseCase: FormatScoreUseCase,
    private val matchOutcomeFromScoreUseCase: MatchOutcomeFromScoreUseCase,

    private val valoModeRepo: ValoModeRepo,
    private val valoMapRepo: ValoMapRepo
) : ViewModel() {
    private val _state = MutableStateFlow(ActivityAddFlowState())
    val state = _state.asStateFlow()

    private var placeholderFetchJob: Job? = null
    private var modeObserveJob: Job? = null
    private var mapObserveJob: Job? = null

    private var modePlaceholder: String = ""
    private var mapPlaceholder: String = ""

    private var maps: List<SimpleValoMap>? = null
    private var modes: List<ValoMode>? = null

    private fun handleBack(
        navBack: () -> Unit
    ) {
        when(_state.value.step) {
            ActivityAddFlowStep.ModeStep -> navBack()
            ActivityAddFlowStep.MapStep -> {
                _state.update { it.copy(step = ActivityAddFlowStep.ModeStep) }
            }
            ActivityAddFlowStep.ScoreStep -> {
                _state.update { it.copy(step = ActivityAddFlowStep.MapStep) }
            }
            ActivityAddFlowStep.OutcomeStep -> _state.update {
                it.copy(step = ActivityAddFlowStep.ScoreStep)
            }
            ActivityAddFlowStep.XpCorrectionStep -> _state.update {
                it.copy(step = ActivityAddFlowStep.ModeStep)
            }
            ActivityAddFlowStep.RrRefundStep -> _state.update {
                it.copy(step = ActivityAddFlowStep.ModeStep)
            }
        }
    }

    private fun updateUiState() {
        val currentMode = modes?.firstOrNull { it.uuid == _state.value.modeUuid }
        val currentMap = maps?.firstOrNull { it.uuid == _state.value.mapUuid }

        val scoreA = _state.value.scoreA
        val scoreB = _state.value.scoreB
        val surrender = _state.value.surrender
        val modeCategory = currentMode?.category ?: ValoModeCategory.Standard

        val isPlacementScoreType = currentMode?.category?.getScoreType() == ValoModeCategory.ScoreType.Placement
        val score = formatScoreUseCase(scoreA, scoreB, modeCategory)
        val matchOutcome = matchOutcomeFromScoreUseCase(scoreA, scoreB, surrender, modeCategory) ?: MatchOutcome.Draw

        val time = _state.value.time.toLocalizedString()

        _state.update { it.copy(
            modeCardStates = modes?.map { mode -> ModeCardState.from(mode) },
            mapCardStates = maps?.filter { map ->
                map.category == currentMode?.category?.let { modeCategory ->  ValoMapCategory.from(modeCategory) }
            }?.map { map -> MapCardState.from(map) },
            isPlacementScoreType = isPlacementScoreType,

            matchCardState = it.matchCardState.copy(
                modeName = currentMode?.displayName ?: modePlaceholder,
                mapName = currentMap?.displayName ?: mapPlaceholder,
                iconState = it.matchCardState.iconState.copy(
                    iconUrl = currentMode?.displayIcon,
                    mapImageUrl = currentMap?.splash,
                    outcome = matchOutcome
                ),
                scoreChipState = it.matchCardState.scoreChipState.copy(
                    score = score,
                    outcome = matchOutcome,
                    wasSurrender = surrender in listOf(MatchEndReason.SURRENDER_A, MatchEndReason.SURRENDER_B)
                ),
                time = time
            ),
        ) }
    }

    private fun selectMode(uuid: Uuid?) {
        val oldCategory = modes?.firstOrNull { it.uuid == _state.value.modeUuid }?.category
        val newCategory = modes?.firstOrNull { it.uuid == uuid }?.category

        val scoreTypeChanged = newCategory?.getScoreType() != oldCategory?.getScoreType()
        val mapTypeChanged = newCategory?.let { ValoMapCategory.from(it) } != oldCategory?.let { modeCategory ->  ValoMapCategory.from(modeCategory) }

        if (scoreTypeChanged) { resetScore() }
        if (mapTypeChanged) { resetMap() }

        _state.update { it.copy(modeUuid = uuid) }
        updateUiState()
    }

    private fun selectMap(uuid: Uuid?) {
        _state.update { it.copy(mapUuid = uuid) }
        updateUiState()
    }

    private fun resetMap() {
        _state.update { it.copy(mapUuid = null) }
    }

    private fun updateScore(rawScore: String?, isScoreB: Boolean) {
        when(val result = parseIntUseCase(rawScore, allowNegative = false)) {
            is Result.Ok -> {
                _state.update { if(isScoreB) it.copy(
                    scoreB = result.data,
                    scoreBError = null
                ) else it.copy(
                    scoreA = result.data,
                    scoreAError = null
                ) }
            }
            is Result.Err -> {
                val error = when (result.error) {
                    ParseIntUseCase.IntParseError.EMPTY -> Res.string.activity_add_flow_score_step_score_error_empty
                    ParseIntUseCase.IntParseError.INVALID -> Res.string.activity_add_flow_score_step_score_error_invalid
                    ParseIntUseCase.IntParseError.NEGATIVE -> Res.string.activity_add_flow_score_step_score_error_negative
                }

                _state.update { if(isScoreB) it.copy(
                    scoreB = null,
                    scoreBError = error
                ) else it.copy(
                    scoreA = null,
                    scoreAError = error
                ) }
            }
        }

        updateUiState()
    }

    private fun updateSurrender(reason: MatchEndReason) {
        _state.update { it.copy(surrender = reason) }
        updateUiState()
    }

    private fun resetScore() {
        _state.update { it.copy(scoreA = null, scoreB = null, scoreAError = null, scoreBError = null, surrender = MatchEndReason.COMPLETED) }
    }

    fun onAction(
        action: ActivityAddFlowAction,
        navBack: () -> Unit
    ) {
        when (action) {
            is ActivityAddFlowAction.Back -> handleBack(navBack)
            is ActivityAddFlowAction.ModeSelected -> {
                selectMode(action.uuid)
            }
            is ActivityAddFlowAction.ModeContinue -> {
                _state.update { it.copy(step = ActivityAddFlowStep.MapStep) }
            }
            is ActivityAddFlowAction.MapSelected -> {
                selectMap(action.uuid)
            }
            is ActivityAddFlowAction.MapContinue -> {
                _state.update { it.copy(step = ActivityAddFlowStep.ScoreStep) }
            }
            is ActivityAddFlowAction.ScoreAChanged -> {
                updateScore(action.rawScore, false)
            }
            is ActivityAddFlowAction.ScoreBChanged -> {
                updateScore(action.rawScore, true)
            }
            is ActivityAddFlowAction.SurrenderChanged -> {
                updateSurrender(action.reason)
            }
        }
    }

    init {
        placeholderFetchJob?.cancel()
        placeholderFetchJob = viewModelScope.launch {
            modePlaceholder = getString(Res.string.activity_add_flow_mode_placeholder)
            mapPlaceholder = getString(Res.string.activity_add_flow_map_placeholder)
        }

        modeObserveJob?.cancel()
        modeObserveJob = viewModelScope.launch {
            valoModeRepo.observeAll(fallbackLocale).map { modeList ->
                modeList.filter {
                    it.category !in setOf(ValoModeCategory.Unknown, ValoModeCategory.Tutorial, ValoModeCategory.Range)
                }.sortedByDescending { it.duration?.split('-')?.firstOrNull()?.toInt() ?: 0 }
            }.collectLatest {
                modes = it
                updateUiState()
            }
        }

        mapObserveJob?.cancel()
        mapObserveJob = viewModelScope.launch {
            valoMapRepo.observeAll(fallbackLocale).distinctUntilChanged()
                .map { mapList ->
                mapList.filter {
                    it.category !in setOf(ValoMapCategory.Unknown, ValoMapCategory.Tutorial, ValoMapCategory.Range)
                }.sortedBy { it.displayName }
            }.collectLatest {
                maps = it
                updateUiState()
            }
        }
    }
}
/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   18.08.26, 20:59
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bittim.valolink.core.data.util.fallbackLocale
import dev.bittim.valolink.core.domain.Result
import dev.bittim.valolink.core.domain.model.*
import dev.bittim.valolink.core.domain.repo.ValoMapRepo
import dev.bittim.valolink.core.domain.repo.ValoModeRepo
import dev.bittim.valolink.feature.activity.domain.usecase.GetSeasonActivitiesForCurrentUserByTimeUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.ParseIntUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.rank.ObserveRanksByTimeUseCase
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.state.ActivityAddFlowFormState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.state.ActivityAddFlowState
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.state.ActivityAddFlowUiStateCalculator
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.state.resetActivityAddFlowSelections
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.jetbrains.compose.resources.getString
import valolink.shared.generated.resources.*
import kotlin.math.absoluteValue
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class)
class ActivityAddFlowViewModel(
    private val parseIntUseCase: ParseIntUseCase,
    private val getSeasonActivitiesForCurrentUserByTimeUseCase: GetSeasonActivitiesForCurrentUserByTimeUseCase,
    private val observeRanksByTimeUseCase: ObserveRanksByTimeUseCase,
    private val uiStateCalculator: ActivityAddFlowUiStateCalculator,

    private val valoModeRepo: ValoModeRepo,
    private val valoMapRepo: ValoMapRepo,
) : ViewModel() {
    private val _state = MutableStateFlow(ActivityAddFlowState())
    val state = _state.asStateFlow()

    val timeZone = TimeZone.currentSystemDefault()

    private var uiStateUpdateJob: Job? = null
    private var placeholderFetchJob: Job? = null
    private var modeObserveJob: Job? = null
    private var mapObserveJob: Job? = null
    private var activityFetchJob: Job? = null
    private var rankFetchJob: Job? = null

    private var modePlaceholder: String = ""
    private var mapPlaceholder: String = ""

    private var maps: List<SimpleValoMap>? = null
    private var modes: List<ValoMode>? = null
    private var activities: List<Activity>? = null
    private var ranks: List<ValoRank>? = null

    /**
     * Navigates to the previous step in the activity entry flow or exits from the first step.
     *
     * @param navBack Called when navigating back from the first step.
     */
    private fun handleBack(
        navBack: () -> Unit
    ) {
        when(_state.value.step) {
            ActivityAddFlowStep.ModeStep -> navBack()
            ActivityAddFlowStep.MapStep -> _state.update {
                it.copy(step = ActivityAddFlowStep.ModeStep)
            }
            ActivityAddFlowStep.ScoreStep -> _state.update {
                it.copy(step = ActivityAddFlowStep.MapStep)
            }
            ActivityAddFlowStep.RankStep -> _state.update {
                it.copy(step = ActivityAddFlowStep.ScoreStep)
            }
            ActivityAddFlowStep.XpStep -> _state.update {
                val prevStep = if (_state.value.form.isRankedSelected) ActivityAddFlowStep.RankStep else ActivityAddFlowStep.ScoreStep
                it.copy(step = prevStep)
            }
            ActivityAddFlowStep.XpCorrectionStep -> {
                // Remove sign from XP value to prevent negative values from going into the main flow
                val resultingState = _state.updateAndGet {
                    it.copy(
                        form = it.form.copy(xp = it.form.xp?.absoluteValue),
                        step = ActivityAddFlowStep.ModeStep,
                    )
                }
                updateUiState(resultingState)
            }
            ActivityAddFlowStep.RrRefundStep -> _state.update {
                it.copy(step = ActivityAddFlowStep.ModeStep)
            }
        }
    }

    /**
     * Recalculates the activity add flow UI state from the current form and supporting data.
     *
     * @param state The activity add flow state to recalculate.
     */
    private fun updateUiState(state: ActivityAddFlowState = _state.value) {
        uiStateUpdateJob?.cancel()
        uiStateUpdateJob = viewModelScope.launch {
            val calculatedState = uiStateCalculator.calculate(
                state,
                modes,
                maps,
                activities,
                ranks,
                modePlaceholder,
                mapPlaceholder,
            )
            _state.value = calculatedState
        }
    }

    private fun updateForm(transform: (ActivityAddFlowFormState) -> ActivityAddFlowFormState) {
        _state.update { it.copy(form = transform(it.form)) }
    }

    /**
     * Updates the selected mode and resets dependent selections when the mode category changes.
     *
     * @param uuid The selected mode identifier.
     */
    private fun selectMode(uuid: Uuid?) {
        val oldMode = modes?.firstOrNull { it.uuid == _state.value.form.modeUuid }
        val newMode = modes?.firstOrNull { it.uuid == uuid }

        _state.update {
            val resetState = resetActivityAddFlowSelections(it, oldMode, newMode)
            resetState.copy(form = resetState.form.copy(modeUuid = uuid))
        }
        updateUiState()
    }

    private fun selectMap(uuid: Uuid?) {
        updateForm { it.copy(mapUuid = uuid) }
        updateUiState()
    }

    /**
     * Parses and updates the score for the selected team.
     *
     * @param rawScore The entered score value.
     * @param isScoreB Whether to update team B's score; otherwise, updates team A's score.
     */
    private fun selectScore(rawScore: String?, isScoreB: Boolean) {
        when(val result = parseIntUseCase(rawScore, allowNegative = false, maxDigits = 3)) {
            is Result.Ok -> {
                updateForm { form ->
                    if (isScoreB) form.copy(scoreB = result.data, scoreBError = null)
                    else form.copy(scoreA = result.data, scoreAError = null)
                }
            }
            is Result.Err -> {
                val error = when (result.error) {
                    ParseIntUseCase.IntParseError.EMPTY -> Res.string.activity_add_flow_score_step_score_error_empty
                    ParseIntUseCase.IntParseError.INVALID -> Res.string.activity_add_flow_score_step_score_error_invalid
                    ParseIntUseCase.IntParseError.NEGATIVE -> Res.string.activity_add_flow_score_step_score_error_negative
                    ParseIntUseCase.IntParseError.TOO_MANY_DIGITS -> Res.string.activity_add_flow_score_step_score_error_too_many_digits
                }

                updateForm { form ->
                    if (isScoreB) form.copy(scoreB = null, scoreBError = error)
                    else form.copy(scoreA = null, scoreAError = error)
                }
            }
        }

        updateUiState()
    }

    private fun selectSurrender(reason: MatchEndReason) {
        updateForm { it.copy(surrender = reason) }
        updateUiState()
    }

    private fun selectRankPlacement(placement: Boolean) {
        updateForm { it.copy(rankPlacement = placement) }
        updateUiState()
    }

    /**
     * Updates the selected rank tier.
     *
     * @param tier The selected rank tier, or `null` to clear the selection.
     */
    private fun selectRankTier(tier: Int?) {
        updateForm { it.copy(selectedRankTier = tier) }
        updateUiState()
    }

    private fun selectVisibleRrDelta(rawRrDelta: String?) {
        when(val result = parseIntUseCase(rawRrDelta, allowNegative = true, maxDigits = 2)) {
            is Result.Ok -> {
                updateForm { it.copy(visibleRrDelta = result.data, rrDeltaError = null) }
            }
            is Result.Err -> {
                val error = when (result.error) {
                    ParseIntUseCase.IntParseError.EMPTY -> Res.string.activity_add_flow_xp_step_xp_error_empty
                    ParseIntUseCase.IntParseError.INVALID -> Res.string.activity_add_flow_xp_step_xp_error_invalid
                    ParseIntUseCase.IntParseError.NEGATIVE -> null
                    ParseIntUseCase.IntParseError.TOO_MANY_DIGITS -> Res.string.activity_add_flow_rank_step_rr_error_too_many_digits
                }

                updateForm { it.copy(rrDeltaError = error) }
            }
        }
        updateUiState()
    }

    /**
     * Updates whether the activity is marked as ranked.
     *
     * @param selected Whether the activity is marked as ranked.
     */
    private fun selectRanked(selected: Boolean) {
        updateForm { it.copy(isRankedSelected = selected) }
        updateUiState()
    }

    /**
     * Parses and stores the XP input, applying the appropriate validation error when needed.
     *
     * @param rawXp The raw XP input to parse.
     * @param allowNegative Whether negative XP values are accepted.
     */
    private fun selectXp(rawXp: String?, allowNegative: Boolean = false) {
        when(val result = parseIntUseCase(rawXp, allowNegative = allowNegative)) {
            is Result.Ok -> {
                updateForm { it.copy(xp = result.data, xpError = null) }
            }
            is Result.Err -> {
                val error = when (result.error) {
                    ParseIntUseCase.IntParseError.EMPTY -> Res.string.activity_add_flow_xp_step_xp_error_empty
                    ParseIntUseCase.IntParseError.INVALID -> Res.string.activity_add_flow_xp_step_xp_error_invalid
                    ParseIntUseCase.IntParseError.NEGATIVE -> Res.string.activity_add_flow_xp_step_xp_error_negative
                    ParseIntUseCase.IntParseError.TOO_MANY_DIGITS -> null
                }

                updateForm { it.copy(xpError = error) }
            }
        }
        updateUiState()
    }

    /**
     * Updates the selected time from the provided date and clock values.
     *
     * @param dateMillis The selected date in epoch milliseconds.
     * @param hour The selected hour of day.
     * @param minute The selected minute.
     */
    private fun selectTime(dateMillis: Long, hour: Int, minute: Int) {
        val localDate = Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(timeZone).date
        val localTime = LocalTime(hour, minute)
        val newTime = LocalDateTime(localDate, localTime).toInstant(timeZone)

        updateForm { it.copy(time = newTime) }
        updateUiState()
    }

    /**
     * Processes an activity-entry action and updates the form or navigation state accordingly.
     *
     * @param action The activity-entry action to process.
     * @param navBack Callback invoked when backward navigation exits the flow.
     */
    fun onAction(
        action: ActivityAddFlowAction,
        navBack: () -> Unit
    ) {
        when (action) {
            is ActivityAddFlowAction.Back -> handleBack(navBack)
            is ActivityAddFlowAction.ToXpCorrection -> {
                _state.update { it.copy(step = ActivityAddFlowStep.XpCorrectionStep) }
            }
            is ActivityAddFlowAction.ToRrRefund -> {
                _state.update { it.copy(step = ActivityAddFlowStep.RrRefundStep) }
            }
            is ActivityAddFlowAction.ModeSelected -> {
                selectMode(action.uuid)
            }
            is ActivityAddFlowAction.RankedChanged -> {
                selectRanked(action.selected)
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
            is ActivityAddFlowAction.ScoreChanged -> {
                selectScore(action.rawScore, action.team == ActivityAddFlowAction.ScoreTeam.B)
            }
            is ActivityAddFlowAction.SurrenderChanged -> {
                selectSurrender(action.reason)
            }
            is ActivityAddFlowAction.ScoreContinue -> {
                val nextStep = if (_state.value.form.isRankedSelected) ActivityAddFlowStep.RankStep else ActivityAddFlowStep.XpStep
                _state.update { it.copy(step = nextStep) }
            }
            is ActivityAddFlowAction.RankPlacementChanged -> {
                selectRankPlacement(action.placement)
            }
            is ActivityAddFlowAction.RankSelected -> {
                selectRankTier(action.tier)
            }
            is ActivityAddFlowAction.RrDeltaChanged -> {
                selectVisibleRrDelta(action.rawRr)
            }
            is ActivityAddFlowAction.RankContinue -> {
                _state.update { it.copy(step = ActivityAddFlowStep.XpStep) }
            }
            is ActivityAddFlowAction.XpChanged -> {
                selectXp(action.rawXp, action.allowNegative)
            }
            is ActivityAddFlowAction.ChangeTime -> {
                _state.update { it.copy(dateTimePickerVisible = true) }
            }
            is ActivityAddFlowAction.DateTimePickerDismiss -> {
                _state.update { it.copy(dateTimePickerVisible = false) }
            }
            is ActivityAddFlowAction.DateTimeSelected -> {
                selectTime(action.dateMillis, action.hour, action.minute)
                _state.update { it.copy(dateTimePickerVisible = false) }
            }
            is ActivityAddFlowAction.XpFinish -> {
                // TODO: Implement
            }
        }
    }

    init {
        val currentLocalDateTime = Clock.System.now().toLocalDateTime(timeZone)
        val currentTimeInstant = currentLocalDateTime.date.atTime(currentLocalDateTime.hour, currentLocalDateTime.minute).toInstant(timeZone)
        updateForm { it.copy(time = currentTimeInstant) }

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
                }.sortedByDescending { it.duration?.split('-')?.firstOrNull()?.toIntOrNull() ?: 0 }
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

        activityFetchJob?.cancel()
        activityFetchJob = viewModelScope.launch {
            state.map { it.form.time }.distinctUntilChanged().collectLatest {
                activities = getSeasonActivitiesForCurrentUserByTimeUseCase(it)
                updateUiState()
            }
        }

        rankFetchJob?.cancel()
        rankFetchJob = viewModelScope.launch {
            state.distinctUntilChanged { old, new -> old.form.time == new.form.time }.flatMapLatest {
                observeRanksByTimeUseCase(it.form.time).distinctUntilChanged()
            }.collectLatest {
                ranks = it
                updateUiState()
            }
        }
    }
}

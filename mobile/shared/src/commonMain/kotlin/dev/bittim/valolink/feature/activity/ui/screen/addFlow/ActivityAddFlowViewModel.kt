/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.06.26, 19:45
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
import dev.bittim.valolink.feature.activity.domain.usecase.*
import dev.bittim.valolink.feature.activity.ui.components.map.MapCardState
import dev.bittim.valolink.feature.activity.ui.components.match.RrChipState
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCardState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.jetbrains.compose.resources.getString
import valolink.shared.generated.resources.*
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

class ActivityAddFlowViewModel(
    private val parseIntUseCase: ParseIntUseCase,
    private val formatScoreUseCase: FormatScoreUseCase,
    private val matchOutcomeFromScoreUseCase: MatchOutcomeFromScoreUseCase,
    private val getSeasonActivitiesForCurrentUserByTimeUseCase: GetSeasonActivitiesForCurrentUserByTimeUseCase,
    private val calculateRrBeforeTimeUseCase: CalculateRrBeforeTimeUseCase,
    private val mapRrToRank: MapRrToRank,

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

    private var modePlaceholder: String = ""
    private var mapPlaceholder: String = ""

    private var maps: List<SimpleValoMap>? = null
    private var modes: List<ValoMode>? = null
    private var activities: List<Activity>? = null

    /**
     * Moves the add flow back by one step or exits the screen from the first step.
     *
     * @param navBack Called when the current step is the first step and the screen should navigate back.
     */
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
            ActivityAddFlowStep.XpStep -> _state.update {
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

    /**
     * Recomputes the derived UI state from the current selections and loaded data.
     *
     * Updates the visible cards, button enablement, match summary, and ranked indicators using the
     * selected mode, map, score, time, and season activities.
     */
    private fun updateUiState() {
        uiStateUpdateJob?.cancel()
        uiStateUpdateJob = viewModelScope.launch {
            activityFetchJob?.join()

            val currentMode = modes?.firstOrNull { it.uuid == _state.value.modeUuid }
            val currentMap = maps?.firstOrNull { it.uuid == _state.value.mapUuid }

            val scoreA = _state.value.scoreA
            val scoreB = _state.value.scoreB
            val surrender = _state.value.surrender
            val modeCategory = currentMode?.category ?: ValoModeCategory.Standard

            val isPlacementScoreType = currentMode?.category?.getScoreType() == ValoModeCategory.ScoreType.Placement
            val score = formatScoreUseCase(scoreA, scoreB, modeCategory)
            val matchOutcome =
                matchOutcomeFromScoreUseCase(scoreA, scoreB, surrender, modeCategory) ?: MatchOutcome.Draw

            val time = _state.value.time
            val localizedTimeString = _state.value.time.toLocalizedString()
            val xp = _state.value.xp
            val rr = _state.value.rr

            val isRankedSelected = _state.value.isRankedSelected
            val (prevRank, rank) = if (isRankedSelected) {
                val totalRr = calculateRrBeforeTimeUseCase(activities, currentMode?.uuid, time)
                Pair(mapRrToRank(totalRr, time), mapRrToRank(totalRr?.plus(rr ?: 0), time))
            } else Pair(null, null)
            val rankChanged = prevRank?.rank?.tier != rank?.rank?.tier

            val enableModeContinueButton = modes != null && _state.value.modeUuid != null
            val enableMapContinueButton = maps != null && _state.value.mapUuid != null
            val enableScoreContinueButton =
                scoreA != null && _state.value.scoreAError == null && (isPlacementScoreType || scoreB != null && _state.value.scoreBError == null)
            val enableResultContinueButton = false

            val iconUrl = if (rank != null) rank.rank.largeIcon else currentMode?.displayIcon

            _state.update {
                it.copy(
                    modeCardStates = modes?.map { mode -> ModeCardState.from(mode) },
                    mapCardStates = maps?.filter { map ->
                        map.category == currentMode?.category?.let { modeCategory -> ValoMapCategory.from(modeCategory) }
                    }?.map { map -> MapCardState.from(map) },
                    isPlacementScoreType = isPlacementScoreType,
                    supportsRanked = currentMode?.canBeRanked ?: false,

                    enableModeContinueButton = enableModeContinueButton,
                    enableMapContinueButton = enableMapContinueButton,
                    enableScoreContinueButton = enableScoreContinueButton,
                    enableResultContinueButton = enableResultContinueButton,

                    matchCardState = it.matchCardState.copy(
                        modeName = currentMode?.displayName ?: modePlaceholder,
                        mapName = currentMap?.displayName ?: mapPlaceholder,
                        iconState = it.matchCardState.iconState.copy(
                            iconUrl = iconUrl,
                            mapImageUrl = currentMap?.splash,
                            outcome = matchOutcome,
                            rrChipState = RrChipState(
                                rr = rank?.rr ?: 0,
                                rankChanged = rankChanged
                            ),
                        ),
                        scoreChipState = it.matchCardState.scoreChipState.copy(
                            score = score,
                            outcome = matchOutcome,
                            wasSurrender = surrender in listOf(MatchEndReason.SURRENDER_A, MatchEndReason.SURRENDER_B)
                        ),
                        time = localizedTimeString,
                        xp = xp,
                    ),
                )
            }
        }
    }

    /**
     * Loads the season activities for the selected time.
     */
    private fun updateActivities() {
        activityFetchJob?.cancel()
        activityFetchJob = viewModelScope.launch {
            activities = getSeasonActivitiesForCurrentUserByTimeUseCase(_state.value.time)
        }
    }

    /**
     * Updates the selected mode and resets dependent selections when the mode category changes.
     *
     * @param uuid The selected mode identifier.
     */
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

    private fun updateRr(rawRr: String?) {
        when(val result = parseIntUseCase(rawRr, allowNegative = true)) {
            is Result.Ok -> {
                _state.update { it.copy(rr = result.data, rrError = null) }
            }
            is Result.Err -> {
                val error = when (result.error) {
                    ParseIntUseCase.IntParseError.EMPTY -> Res.string.activity_add_flow_xp_step_xp_error_empty
                    ParseIntUseCase.IntParseError.INVALID -> Res.string.activity_add_flow_xp_step_xp_error_invalid
                    ParseIntUseCase.IntParseError.NEGATIVE -> null
                }

                _state.update { it.copy(rrError = error) }
            }
        }
        updateUiState()
    }

    private fun updateRanked(selected: Boolean) {
        _state.update { it.copy(isRankedSelected = selected) }
        updateUiState()
    }

    private fun updateXp(rawXp: String?) {
        when(val result = parseIntUseCase(rawXp, allowNegative = false)) {
            is Result.Ok -> {
                _state.update { it.copy(xp = result.data, xpError = null) }
            }
            is Result.Err -> {
                val error = when (result.error) {
                    ParseIntUseCase.IntParseError.EMPTY -> Res.string.activity_add_flow_xp_step_xp_error_empty
                    ParseIntUseCase.IntParseError.INVALID -> Res.string.activity_add_flow_xp_step_xp_error_invalid
                    ParseIntUseCase.IntParseError.NEGATIVE -> Res.string.activity_add_flow_xp_step_xp_error_negative
                }

                _state.update { it.copy(xpError = error) }
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
    private fun updateTime(dateMillis: Long, hour: Int, minute: Int) {
        val localDate = Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(timeZone).date
        val localTime = LocalTime(hour, minute)
        val newTime = LocalDateTime(localDate, localTime).toInstant(timeZone)

        _state.update { it.copy(time = newTime) }
        updateActivities()
        updateUiState()
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
            is ActivityAddFlowAction.RankedChanged -> {
                updateRanked(action.selected)
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
            is ActivityAddFlowAction.ScoreContinue -> {
                _state.update { it.copy(step = ActivityAddFlowStep.XpStep) }
            }
            is ActivityAddFlowAction.RrChanged -> {
                updateRr(action.rawRr)
            }
            is ActivityAddFlowAction.XpChanged -> {
                updateXp(action.rawXp)
            }
            is ActivityAddFlowAction.ChangeTime -> {
                _state.update { it.copy(dateTimePickerVisible = true) }
            }
            is ActivityAddFlowAction.DateTimePickerDismiss -> {
                _state.update { it.copy(dateTimePickerVisible = false) }
            }
            is ActivityAddFlowAction.DateTimeSelected -> {
                updateTime(action.dateMillis, action.hour, action.minute)
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
        _state.update { it.copy(time = currentTimeInstant) }

        placeholderFetchJob?.cancel()
        placeholderFetchJob = viewModelScope.launch {
            modePlaceholder = getString(Res.string.activity_add_flow_mode_placeholder)
            mapPlaceholder = getString(Res.string.activity_add_flow_map_placeholder)
        }

        updateActivities()

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
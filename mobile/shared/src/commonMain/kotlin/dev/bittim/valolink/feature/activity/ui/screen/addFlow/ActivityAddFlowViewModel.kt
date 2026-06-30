/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.06.26, 14:20
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
import dev.bittim.valolink.feature.activity.domain.usecase.GetSeasonActivitiesForCurrentUserByTimeUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.MatchOutcomeFromScoreUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.ParseIntUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.rank.CalculateRrBeforeTimeUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.rank.CalculateRrDeltaUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.rank.MapRrToRank
import dev.bittim.valolink.feature.activity.domain.usecase.rank.ObserveRanksByTimeUseCase
import dev.bittim.valolink.feature.activity.ui.components.map.MapCardState
import dev.bittim.valolink.feature.activity.ui.components.match.RrChipState
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCardState
import dev.bittim.valolink.feature.activity.ui.components.rank.RankCardState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.jetbrains.compose.resources.getString
import valolink.shared.generated.resources.*
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class)
class ActivityAddFlowViewModel(
    private val parseIntUseCase: ParseIntUseCase,
    private val formatScoreUseCase: FormatScoreUseCase,
    private val matchOutcomeFromScoreUseCase: MatchOutcomeFromScoreUseCase,
    private val getSeasonActivitiesForCurrentUserByTimeUseCase: GetSeasonActivitiesForCurrentUserByTimeUseCase,
    private val calculateRrBeforeTimeUseCase: CalculateRrBeforeTimeUseCase,
    private val mapRrToRank: MapRrToRank,
    private val observeRanksByTimeUseCase: ObserveRanksByTimeUseCase,
    private val calculateRrDeltaUseCase: CalculateRrDeltaUseCase,

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
     * Moves the add flow back by one step or exits the screen from the first step.
     *
     * @param navBack Called when the current step is the first step and the screen should navigate back.
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
                val prevStep = if (_state.value.isRankedSelected) ActivityAddFlowStep.RankStep else ActivityAddFlowStep.ScoreStep
                it.copy(step = prevStep)
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
            val visibleRrDelta = _state.value.visibleRrDelta

            val isRankedSelected = _state.value.isRankedSelected
            val (rank, newRank, rrDelta) = if (isRankedSelected) {
                val totalRr = calculateRrBeforeTimeUseCase(activities, currentMode?.uuid, time)
                val rank = mapRrToRank(totalRr, time)

                val rrDelta = visibleRrDelta?.let { rr -> rank?.let { rank -> calculateRrDeltaUseCase(rank, rr) } }

                val currentTotalRr = if (totalRr == null) { visibleRrDelta } else totalRr + (rrDelta ?: 0)
                val newRank = mapRrToRank(currentTotalRr, time)

                Triple(rank, newRank, rrDelta)
            } else Triple(null, null, null)
            val rankChanged = rank?.rank?.tier != newRank?.rank?.tier

            val enableModeContinueButton = modes != null && _state.value.modeUuid != null
            val enableMapContinueButton = maps != null && _state.value.mapUuid != null
            val enableScoreContinueButton =
                scoreA != null && _state.value.scoreAError == null && (isPlacementScoreType || scoreB != null && _state.value.scoreBError == null)
            val enableRankContinueButton = false
            val enableResultContinueButton = false

            val iconUrl = if (newRank != null) newRank.rank.largeIcon else currentMode?.displayIcon

            _state.update { state ->
                state.copy(
                    modeCardStates = modes?.map { mode -> ModeCardState.from(mode) },
                    mapCardStates = maps?.filter { map ->
                        map.category == currentMode?.category?.let { modeCategory -> ValoMapCategory.from(modeCategory) }
                    }?.map { map -> MapCardState.from(map) },
                    rankCardStates = ranks?.map { rank -> RankCardState.from(rank) },
                    isPlacementScoreType = isPlacementScoreType,
                    supportsRanked = currentMode?.canBeRanked ?: false,

                    enableModeContinueButton = enableModeContinueButton,
                    enableMapContinueButton = enableMapContinueButton,
                    enableScoreContinueButton = enableScoreContinueButton,
                    enableRankContinueButton = enableRankContinueButton,
                    enableResultContinueButton = enableResultContinueButton,

                    showRankModifier = rankChanged,

                    matchOutcome = matchOutcome,
                    currentRank = rank,
                    rrDelta = rrDelta,

                    matchCardState = state.matchCardState.copy(
                        modeName = currentMode?.displayName ?: modePlaceholder,
                        mapName = currentMap?.displayName ?: mapPlaceholder,
                        iconState = state.matchCardState.iconState.copy(
                            iconUrl = iconUrl,
                            mapImageUrl = currentMap?.splash,
                            outcome = matchOutcome,
                            rrChipState = visibleRrDelta?.let {
                                RrChipState(
                                    rr = it,
                                    rankChanged = rankChanged
                                )
                            },
                        ),
                        scoreChipState = state.matchCardState.scoreChipState.copy(
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
     * Updates the selected mode and resets dependent selections when the mode category changes.
     *
     * @param uuid The selected mode identifier.
     */
    private fun selectMode(uuid: Uuid?) {
        val oldMode = modes?.firstOrNull { it.uuid == _state.value.modeUuid }
        val newMode = modes?.firstOrNull { it.uuid == uuid }

        val scoreTypeChanged = newMode?.category?.getScoreType() != oldMode?.category?.getScoreType()
        val mapTypeChanged = newMode?.category?.let { ValoMapCategory.from(it) } != oldMode?.category?.let { modeCategory ->  ValoMapCategory.from(modeCategory) }
        val supportsRankedChanged = newMode?.canBeRanked != oldMode?.canBeRanked

        if (scoreTypeChanged) { resetScore() }
        if (mapTypeChanged) { resetMap() }
        if (supportsRankedChanged) { selectRanked(false) }

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

    private fun selectScore(rawScore: String?, isScoreB: Boolean) {
        when(val result = parseIntUseCase(rawScore, allowNegative = false, maxDigits = 3)) {
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
                    ParseIntUseCase.IntParseError.TOO_MANY_DIGITS -> Res.string.activity_add_flow_score_step_score_error_too_many_digits
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

    private fun selectSurrender(reason: MatchEndReason) {
        _state.update { it.copy(surrender = reason) }
        updateUiState()
    }

    private fun resetScore() {
        _state.update { it.copy(scoreA = null, scoreB = null, scoreAError = null, scoreBError = null, surrender = MatchEndReason.COMPLETED) }
    }

    private fun selectVisibleRrDelta(rawRrDelta: String?) {
        when(val result = parseIntUseCase(rawRrDelta, allowNegative = true, maxDigits = 2)) {
            is Result.Ok -> {
                _state.update { it.copy(visibleRrDelta = result.data, rrDeltaError = null) }
            }
            is Result.Err -> {
                val error = when (result.error) {
                    ParseIntUseCase.IntParseError.EMPTY -> Res.string.activity_add_flow_xp_step_xp_error_empty
                    ParseIntUseCase.IntParseError.INVALID -> Res.string.activity_add_flow_xp_step_xp_error_invalid
                    ParseIntUseCase.IntParseError.NEGATIVE -> null
                    ParseIntUseCase.IntParseError.TOO_MANY_DIGITS -> Res.string.activity_add_flow_xp_step_xp_error_too_many_digits
                }

                _state.update { it.copy(rrDeltaError = error) }
            }
        }
        updateUiState()
    }

    private fun selectRanked(selected: Boolean) {
        _state.update { it.copy(isRankedSelected = selected) }
        updateUiState()
    }

    private fun selectXp(rawXp: String?) {
        when(val result = parseIntUseCase(rawXp, allowNegative = false)) {
            is Result.Ok -> {
                _state.update { it.copy(xp = result.data, xpError = null) }
            }
            is Result.Err -> {
                val error = when (result.error) {
                    ParseIntUseCase.IntParseError.EMPTY -> Res.string.activity_add_flow_xp_step_xp_error_empty
                    ParseIntUseCase.IntParseError.INVALID -> Res.string.activity_add_flow_xp_step_xp_error_invalid
                    ParseIntUseCase.IntParseError.NEGATIVE -> Res.string.activity_add_flow_xp_step_xp_error_negative
                    ParseIntUseCase.IntParseError.TOO_MANY_DIGITS -> null
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
    private fun selectTime(dateMillis: Long, hour: Int, minute: Int) {
        val localDate = Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(timeZone).date
        val localTime = LocalTime(hour, minute)
        val newTime = LocalDateTime(localDate, localTime).toInstant(timeZone)

        _state.update { it.copy(time = newTime) }
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
            is ActivityAddFlowAction.ScoreAChanged -> {
                selectScore(action.rawScore, false)
            }
            is ActivityAddFlowAction.ScoreBChanged -> {
                selectScore(action.rawScore, true)
            }
            is ActivityAddFlowAction.SurrenderChanged -> {
                selectSurrender(action.reason)
            }
            is ActivityAddFlowAction.ScoreContinue -> {
                val nextStep = if (_state.value.isRankedSelected) ActivityAddFlowStep.RankStep else ActivityAddFlowStep.XpStep
                _state.update { it.copy(step = nextStep) }
            }
            is ActivityAddFlowAction.RrDeltaChanged -> {
                selectVisibleRrDelta(action.rawRr)
            }
            is ActivityAddFlowAction.XpChanged -> {
                selectXp(action.rawXp)
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
        _state.update { it.copy(time = currentTimeInstant) }

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
            state.map { it.time }.distinctUntilChanged().collectLatest {
                activities = getSeasonActivitiesForCurrentUserByTimeUseCase(it)
                updateUiState()
            }
        }

        rankFetchJob?.cancel()
        rankFetchJob = viewModelScope.launch {
            state.distinctUntilChanged { old, new -> old.time == new.time }.flatMapLatest {
                observeRanksByTimeUseCase(it.time).distinctUntilChanged()
            }.collectLatest {
                ranks = it
                updateUiState()
            }
        }
    }
}
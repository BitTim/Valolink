/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   10.06.26, 11:17
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bittim.valolink.core.data.util.fallbackLocale
import dev.bittim.valolink.core.domain.model.SimpleValoMap
import dev.bittim.valolink.core.domain.model.ValoMapCategory
import dev.bittim.valolink.core.domain.model.ValoMode
import dev.bittim.valolink.core.domain.model.ValoModeCategory
import dev.bittim.valolink.core.domain.repo.ValoMapRepo
import dev.bittim.valolink.core.domain.repo.ValoModeRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ActivityAddFlowViewModel(
    private val valoModeRepo: ValoModeRepo,
    private val valoMapRepo: ValoMapRepo
) : ViewModel() {
    private val _state = MutableStateFlow(ActivityAddFlowState())
    val state = _state.asStateFlow()

    private var modeObserveJob: Job? = null
    private var mapObserveJob: Job? = null

    private fun handleBack(
        navBack: () -> Unit
    ) {
        when(_state.value.step) {
            ActivityAddFlowStep.ModeStep -> navBack()
            ActivityAddFlowStep.MapStep -> {
                _state.update { it.copy(step = ActivityAddFlowStep.ModeStep) }
                selectMap(null)
            }
            ActivityAddFlowStep.DetailsStep -> _state.update {
                it.copy(step = ActivityAddFlowStep.MapStep)
            }
            ActivityAddFlowStep.XpCorrectionStep -> _state.update {
                it.copy(step = ActivityAddFlowStep.ModeStep)
            }
            ActivityAddFlowStep.RrRefundStep -> _state.update {
                it.copy(step = ActivityAddFlowStep.ModeStep)
            }
        }
    }

    private fun selectMode(mode: ValoMode?) {
        _state.update {
            it.copy(
                selectedModeUuid = mode?.uuid,
                matchCardState = it.matchCardState.copy(
                    modeName = mode?.displayName ?: "No mode selected",
                    iconState = it.matchCardState.iconState.copy(
                        iconUrl = mode?.displayIcon
                    )
                )
            )
        }
    }

    private fun selectMap(map: SimpleValoMap?) {
        _state.update {
            it.copy(
                selectedMapUuid = map?.uuid,
                matchCardState = it.matchCardState.copy(
                    mapName = map?.displayName ?: "No map",
                    iconState = it.matchCardState.iconState.copy(
                        mapImageUrl = map?.splash
                    )
                )
            )
        }
    }

    fun onAction(
        action: ActivityAddFlowAction,
        navBack: () -> Unit
    ) {
        when (action) {
            is ActivityAddFlowAction.Back -> handleBack(navBack)
            is ActivityAddFlowAction.ModeSelected -> {
                selectMode(action.mode)
            }
            is ActivityAddFlowAction.ModeContinue -> {
                _state.update { it.copy(step = ActivityAddFlowStep.MapStep) }
            }
            is ActivityAddFlowAction.MapSelected -> {
                selectMap(action.map)
            }
            is ActivityAddFlowAction.MapContinue -> {
                _state.update { it.copy(step = ActivityAddFlowStep.DetailsStep) }
            }
        }
    }

    init {
        modeObserveJob?.cancel()
        modeObserveJob = viewModelScope.launch {
            valoModeRepo.observeAll(fallbackLocale).map { modeList ->
                modeList.filter {
                    it.category !in setOf(ValoModeCategory.Unknown, ValoModeCategory.Tutorial, ValoModeCategory.Range)
                }.sortedByDescending { it.duration?.split('-')?.firstOrNull()?.toInt() ?: 0 }
            }.collectLatest { modes ->
                _state.update {
                    it.copy(modes = modes)
                }
            }
        }

        mapObserveJob?.cancel()
        mapObserveJob = viewModelScope.launch {
            valoMapRepo.observeAll(fallbackLocale).map { mapList ->
                mapList.filter {
                    it.category !in setOf(ValoMapCategory.Unknown, ValoMapCategory.Tutorial, ValoMapCategory.Range)
                }.sortedBy { it.displayName }
            }.collectLatest { maps ->
                _state.update {
                    it.copy(maps = maps)
                }
            }
        }
    }
}
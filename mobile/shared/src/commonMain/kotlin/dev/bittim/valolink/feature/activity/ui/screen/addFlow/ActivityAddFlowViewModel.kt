/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   09.06.26, 21:22
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bittim.valolink.core.data.util.fallbackLocale
import dev.bittim.valolink.core.domain.model.ValoMapCategory
import dev.bittim.valolink.core.domain.model.ValoModeCategory
import dev.bittim.valolink.core.domain.repo.ValoMapRepo
import dev.bittim.valolink.core.domain.repo.ValoModeRepo
import dev.bittim.valolink.feature.auth.ui.screen.AuthFlowStep
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

    private fun internalNavBack(): Boolean {
        val oldIndex = _state.value.step.index
        val newIndex = oldIndex.minus(1).coerceIn(0, AuthFlowStep.entries.lastIndex)
        if (newIndex == oldIndex) return false

        _state.update {
            it.copy(
                step = ActivityAddFlowStep.entries[newIndex],
            )
        }

        return true
    }

    fun onAction(
        action: ActivityAddFlowAction,
        navBack: () -> Unit
    ) {
        when (action) {
            is ActivityAddFlowAction.Back -> if (!internalNavBack()) navBack()
            is ActivityAddFlowAction.ModeSelected -> {
                _state.update {
                    it.copy(
                        selectedModeUuid = action.mode.uuid,
                        matchCardState = it.matchCardState.copy(
                            modeName = action.mode.displayName,
                            iconState = it.matchCardState.iconState.copy(
                                iconUrl = action.mode.displayIcon
                            )
                        )
                    )
                }
            }
            is ActivityAddFlowAction.ModeProceed -> {
                _state.update {
                    it.copy(
                        step = ActivityAddFlowStep.MapStep
                    )
                }
            }
            is ActivityAddFlowAction.MapSelected -> {
                _state.update {
                    it.copy(
                        selectedMapUuid = action.map.uuid,
                        matchCardState = it.matchCardState.copy(
                            mapName = action.map.displayName,
                            iconState = it.matchCardState.iconState.copy(
                                mapImageUrl = action.map.splash
                            )
                        )
                    )
                }
            }
            is ActivityAddFlowAction.MapProceed -> {
                _state.update {
                    it.copy(
                        step = ActivityAddFlowStep.DetailsStep
                    )
                }
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
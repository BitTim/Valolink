/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 20:46
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bittim.valolink.core.data.util.fallbackLocale
import dev.bittim.valolink.core.domain.repo.ValoModeRepo
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCardState
import dev.bittim.valolink.feature.auth.ui.screen.AuthFlowStep
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ActivityAddFlowViewModel(
    private val valoModeRepo: ValoModeRepo
) : ViewModel() {
    private val _state = MutableStateFlow(ActivityAddFlowState())
    val state = _state.asStateFlow()

    private var modeObserveJob: Job? = null

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
                        selectedModeUuid = action.modeState.uuid,
                        matchCardState = it.matchCardState.copy(
                            modeName = action.modeState.title,
                            iconState = it.matchCardState.iconState.copy(
                                iconUrl = action.modeState.iconUrl
                            )
                        ),
                        step = ActivityAddFlowStep.MapStep
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
                    it.displayIcon != null && it.duration != null
                }.map { mode ->
                    ModeCardState(
                        uuid = mode.uuid,
                        iconUrl = mode.displayIcon,
                        title = mode.displayName,
                        duration = mode.duration,
                        canBeRanked = mode.canBeRanked
                    )
                }.sortedByDescending { it.duration?.split('-')?.firstOrNull()?.toInt() ?: 0 }
            }.collectLatest { modeStates ->
                _state.update {
                    it.copy(modeStates = modeStates)
                }
            }
        }
    }
}
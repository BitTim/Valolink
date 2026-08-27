/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 17:43
 */

package dev.bittim.valolink.feature.activity.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bittim.valolink.feature.activity.domain.usecase.GetSeasonActivitiesForCurrentUserByTimeUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActivityListViewModel(
    private val getSeasonActivitiesForCurrentUserByTimeUseCase: GetSeasonActivitiesForCurrentUserByTimeUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ActivityListState())
    val state = _state.asStateFlow()

    private var getActivitiesJob: Job? = null

    init {
        getActivitiesJob?.cancel()
        getActivitiesJob = viewModelScope.launch {
            val activities = getSeasonActivitiesForCurrentUserByTimeUseCase()
            _state.update { it.copy(activities = activities) }
        }
    }
}
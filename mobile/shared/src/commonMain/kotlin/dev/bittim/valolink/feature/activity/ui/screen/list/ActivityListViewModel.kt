/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.08.26, 16:59
 */

package dev.bittim.valolink.feature.activity.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.ValoRank
import dev.bittim.valolink.feature.activity.domain.logic.RankCalculator
import dev.bittim.valolink.feature.activity.domain.usecase.GetSeasonActivitiesForCurrentUserByTimeUseCase
import dev.bittim.valolink.feature.activity.domain.usecase.ObserveRanksByTimeUseCase
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCardState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Clock

class ActivityListViewModel(
    private val observeRanksByTimeUseCase: ObserveRanksByTimeUseCase,
    private val getSeasonActivitiesForCurrentUserByTimeUseCase: GetSeasonActivitiesForCurrentUserByTimeUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ActivityListState())
    val state = _state.asStateFlow()

    private var ranks: List<ValoRank>? = null

    private var rankFetchJob: Job? = null
    private var activitiesFetchJob: Job? = null

    fun onAction(action: ActivityListAction) {
        when (action) {
            is ActivityListAction.Refresh -> refreshActivities()
        }
    }

    init {
        rankFetchJob?.cancel()
        rankFetchJob = viewModelScope.launch {
            observeRanksByTimeUseCase(Clock.System.now()).distinctUntilChanged().collectLatest {
                ranks = it
            }
        }

        refreshActivities()
    }

    private fun refreshActivities() {
        activitiesFetchJob?.cancel()
        activitiesFetchJob = viewModelScope.launch {
            val activities = getSeasonActivitiesForCurrentUserByTimeUseCase()
            val rankChanges = RankCalculator.calculateRankChanges(activities, ranks)

            val items = activities.map {
                when (it) {
                    is Activity.MatchActivity -> ActivityListItemState.MatchCard(MatchCardState.fromActivity(it, rankChanges?.get(it.id)))
                    is Activity.XpCorrectionActivity -> ActivityListItemState.XpCorrection(it.xp)
                    is Activity.RrRefundActivity -> ActivityListItemState.RrRefund(it.rr, it.mode.displayName)
                }
            }

            _state.update { it.copy(items = items) }
        }
    }
}
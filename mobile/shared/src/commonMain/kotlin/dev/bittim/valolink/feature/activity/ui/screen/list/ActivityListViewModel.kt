/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityListViewModel.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   05.10.26, 03:05
 */

package dev.bittim.valolink.feature.activity.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.ValoRankTable
import dev.bittim.valolink.core.domain.repo.ValoRankRepo
import dev.bittim.valolink.feature.activity.domain.logic.RankCalculator
import dev.bittim.valolink.feature.activity.domain.usecase.GetSeasonActivitiesForCurrentUserByTimeUseCase
import dev.bittim.valolink.feature.activity.ui.components.match.MatchCardState
import dev.bittim.valolink.feature.activity.ui.screen.list.state.ActivityListItemState
import dev.bittim.valolink.feature.activity.ui.screen.list.state.ActivityListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ActivityListViewModel(
    private val valoRankRepo: ValoRankRepo,
    private val getSeasonActivitiesForCurrentUserByTimeUseCase: GetSeasonActivitiesForCurrentUserByTimeUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<ActivityListState>(ActivityListState.Loading)
    val state = _state.asStateFlow()

    private var ranks: List<ValoRankTable>? = null

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
            valoRankRepo.observeAllTables().collectLatest { ranks = it }
        }

        refreshActivities()
    }

    private fun refreshActivities() {
        activitiesFetchJob?.cancel()
        activitiesFetchJob = viewModelScope.launch {
            _state.value = ActivityListState.Loading

            val activities = getSeasonActivitiesForCurrentUserByTimeUseCase()
            val rankChanges = RankCalculator.calculateRankChanges(activities, ranks)

            val items = activities.map {
                when (it) {
                    is Activity.MatchActivity -> ActivityListItemState.MatchCard(it.id, MatchCardState.fromActivity(it, rankChanges?.get(it.id)))
                    is Activity.XpCorrectionActivity -> ActivityListItemState.XpCorrection(it.id, it.xp)
                    is Activity.RrRefundActivity -> ActivityListItemState.RrRefund(it.id, it.rr, it.mode.displayName)
                }
            }

            // TODO: Error handling
            _state.value = when {
                items.isEmpty() -> ActivityListState.Empty
                else -> ActivityListState.Content(items)
            }
        }
    }
}
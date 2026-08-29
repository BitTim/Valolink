/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowUiStateCalculator.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   29.08.26, 16:09
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.state

import dev.bittim.valolink.core.domain.extension.toLocalizedString
import dev.bittim.valolink.core.domain.model.*
import dev.bittim.valolink.feature.activity.domain.logic.RankCalculator
import dev.bittim.valolink.feature.activity.domain.logic.formatScore
import dev.bittim.valolink.feature.activity.domain.model.RankChange
import dev.bittim.valolink.feature.activity.ui.components.map.MapCardState
import dev.bittim.valolink.feature.activity.ui.components.match.RrChipState
import dev.bittim.valolink.feature.activity.ui.components.mode.ModeCardState
import dev.bittim.valolink.feature.activity.ui.components.rank.RankCardState

/** Builds presentation state from add-flow input and loaded feature data. */
class ActivityAddFlowUiStateCalculator(
) {
    /**
     * Builds the presentation state for the activity add flow from the current form and loaded game data.
     *
     * @param state The current activity add flow state.
     * @param modes Available game modes.
     * @param maps Available maps.
     * @param activities Existing activities used to calculate rank changes.
     * @param ranks Available rank tiers.
     * @param modePlaceholder Text displayed when no mode is selected.
     * @param mapPlaceholder Text displayed when no map is selected.
     * @return The updated activity add flow state.
     */
    fun calculate(
        state: ActivityAddFlowState,
        modes: List<ValoMode>?,
        maps: List<SimpleValoMap>?,
        activities: List<Activity>?,
        ranks: List<ValoRank>?,
        modePlaceholder: String,
        mapPlaceholder: String,
    ): ActivityAddFlowState {
        val currentMode = modes?.firstOrNull { it.uuid == state.form.modeUuid }
        val currentMap = maps?.firstOrNull { it.uuid == state.form.mapUuid }
        val modeCategory = currentMode?.category ?: ValoModeCategory.Standard
        val isPlacementScoreType = currentMode?.category?.getScoreType() == ValoModeCategory.ScoreType.Placement
        val score = formatScore(state.form.scoreA, state.form.scoreB, modeCategory)
        val matchOutcome = MatchOutcome.fromScore(
            state.form.scoreA,
            state.form.scoreB,
            state.form.endReason,
            modeCategory,
        )

        val selectedRankTier = if (state.form.rankPlacement) state.form.selectedRankTier else null
        val totalRr = RankCalculator.calculateRrUpToTime(activities, currentMode?.uuid, state.form.time)
        val rankChange = when {
            !state.form.isRankedSelected -> null
            totalRr != null -> RankChange.fromRawRr(totalRr, state.form.visibleRrDelta, state.form.rankModifier, ranks)
            else -> RankChange.fromPlacement(state.form.rankPlacement, selectedRankTier, ranks)
        }
        val rankChanged = rankChange?.current?.rank?.tier != rankChange?.new?.rank?.tier
        val iconUrl = rankChange?.new?.rank?.largeIcon ?: currentMode?.displayIcon

        return state.copy(
            modeCardStates = modes?.map { ModeCardState.from(it) },
            mapCardStates = maps?.filter {
                it.category == currentMode?.category?.let(ValoMapCategory::from)
            }?.map { MapCardState.from(it) },
            rankCardStates = ranks
                ?.filter {
                    !it.division.contains("UNRANKED") && !it.division.contains("INVALID")
                }
                ?.map { RankCardState.from(it) },
            isPlacementScoreType = isPlacementScoreType,
            showRankModifier = rankChanged || state.form.rankModifier,
            matchOutcome = matchOutcome,
            currentRank = rankChange?.current,
            rrDelta = rankChange?.rrDelta,
            matchCardState = state.matchCardState.copy(
                modeName = currentMode?.displayName ?: modePlaceholder,
                mapName = currentMap?.displayName ?: mapPlaceholder,
                iconState = state.matchCardState.iconState.copy(
                    iconUrl = iconUrl,
                    mapImageUrl = currentMap?.splash,
                    outcome = matchOutcome,
                    rrChipState = state.form.visibleRrDelta?.let {
                        RrChipState(rr = it, rankChanged = rankChanged)
                    },
                ),
                scoreChipState = state.matchCardState.scoreChipState.copy(
                    score = score,
                    outcome = matchOutcome,
                    wasSurrender = state.form.endReason in listOf(
                        MatchEndReason.SURRENDER_A,
                        MatchEndReason.SURRENDER_B,
                    ),
                ),
                time = state.form.time.toLocalizedString(),
                xp = state.form.xp,
            ),
        )
    }
}

/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowSelectionReset.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 16:08
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.state

import dev.bittim.valolink.core.domain.model.MatchEndReason
import dev.bittim.valolink.core.domain.model.ValoMapCategory
import dev.bittim.valolink.core.domain.model.ValoMode

/** Resets form values that become invalid when the selected mode changes. */
/**
 * Resets form selections that are incompatible with a change in the selected mode.
 *
 * @param state The current activity add-flow state.
 * @param oldMode The previously selected mode.
 * @param newMode The newly selected mode.
 * @return The updated state with incompatible scores, map, and ranked inputs cleared.
 */
fun resetActivityAddFlowSelections(
    state: ActivityAddFlowState,
    oldMode: ValoMode?,
    newMode: ValoMode?,
): ActivityAddFlowState {
    val scoreTypeChanged = newMode?.category?.getScoreType() != oldMode?.category?.getScoreType()
    val mapTypeChanged = newMode?.category?.let(ValoMapCategory::from) !=
        oldMode?.category?.let(ValoMapCategory::from)
    val supportsRankedChanged = newMode?.canBeRanked != oldMode?.canBeRanked

    return state.copy(
        form = state.form.copy(
            scoreA = if (scoreTypeChanged) null else state.form.scoreA,
            scoreB = if (scoreTypeChanged) null else state.form.scoreB,
            scoreAError = if (scoreTypeChanged) null else state.form.scoreAError,
            scoreBError = if (scoreTypeChanged) null else state.form.scoreBError,
            endReason = if (scoreTypeChanged) MatchEndReason.COMPLETED else state.form.endReason,
            mapUuid = if (mapTypeChanged) null else state.form.mapUuid,
            isRankedSelected = !supportsRankedChanged && state.form.isRankedSelected,
            rankPlacement = !supportsRankedChanged && state.form.rankPlacement,
            selectedRankTier = if (supportsRankedChanged) null else state.form.selectedRankTier,
            visibleRrDelta = if (supportsRankedChanged) null else state.form.visibleRrDelta,
            rrDeltaError = if (supportsRankedChanged) null else state.form.rrDeltaError,
        ),
    )
}

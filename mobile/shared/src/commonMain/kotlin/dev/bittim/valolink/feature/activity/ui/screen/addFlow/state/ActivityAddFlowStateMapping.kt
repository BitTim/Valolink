/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowStateMapping.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.08.26, 12:22
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.state

import dev.bittim.valolink.core.domain.model.ActivityType
import dev.bittim.valolink.feature.activity.domain.model.FinalizeActivityInput

/**
 * Maps the activity form state to the input required to finalize the specified activity type.
 *
 * @param type The activity type to finalize.
 * @return The corresponding finalization input, or `null` when required form data is missing.
 */
fun ActivityAddFlowState.toFinalizeActivityInput(
    type: ActivityType
): FinalizeActivityInput? {
    return when(type) {
        ActivityType.MATCH -> {
            FinalizeActivityInput.Match(
                time = form.time,
                xp = form.xp ?: return null,
                rr = rrDelta,
                mode = form.modeUuid ?: return null,
                scoreA = form.scoreA ?: return null,
                scoreB = form.scoreB,
                endReason = form.endReason,
                isRanked = form.isRankedSelected,
                map = form.mapUuid ?: return null,
                visibleRr = form.visibleRrDelta
            )
        }
        ActivityType.RR_REFUND -> FinalizeActivityInput.RrRefund(
            time = form.time,
            rr = form.visibleRrDelta,
            mode = form.modeUuid ?: return null
        )
        ActivityType.XP_CORRECTION -> FinalizeActivityInput.XpCorrection(
            time = form.time,
            xp = form.xp ?: return null
        )
    }
}
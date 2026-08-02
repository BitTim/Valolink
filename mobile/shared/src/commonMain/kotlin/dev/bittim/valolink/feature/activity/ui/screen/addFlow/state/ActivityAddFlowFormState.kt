/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityAddFlowFormState.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.08.26, 13:02
 */

package dev.bittim.valolink.feature.activity.ui.screen.addFlow.state

import dev.bittim.valolink.core.domain.model.MatchEndReason
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

/** Mutable user input and validation state for the add-flow form. */
data class ActivityAddFlowFormState(
    val modeUuid: Uuid? = null,
    val mapUuid: Uuid? = null,
    val scoreA: Int? = null,
    val scoreB: Int? = null,
    val surrender: MatchEndReason = MatchEndReason.COMPLETED,
    val xp: Int? = null,
    val isRankedSelected: Boolean = false,
    val rankPlacement: Boolean = false,
    val selectedRankTier: Int? = null,
    val visibleRrDelta: Int? = null,
    val time: Instant = Clock.System.now(),
    val scoreAError: StringResource? = null,
    val scoreBError: StringResource? = null,
    val xpError: StringResource? = null,
    val rrDeltaError: StringResource? = null,
)

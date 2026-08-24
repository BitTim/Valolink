/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchBundle.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 16:44
 */

package dev.bittim.valolink.feature.activity.domain.model

import dev.bittim.valolink.core.domain.model.Match
import dev.bittim.valolink.core.domain.model.MatchParticipant

data class MatchBundle(
    val match: Match,
    val participant: MatchParticipant
)

/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       InstantExtensions.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   04.06.26, 12:01
 */

package dev.bittim.valolink.core.domain.extension

import kotlin.time.Instant

// commonMain
expect fun Instant.toLocalizedString(): String
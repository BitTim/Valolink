/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoVersion.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   06.06.26, 13:57
 */

package dev.bittim.valolink.core.domain.model

import kotlin.time.Instant

data class ValoVersion(
    val branch: String,
    val buildVersion: String,
    val manifestId: String,
    val riotClientBuild: String,
    val riotClientVersion: String,
    val engineVersion: String,
    val version: String,
    val buildDate: Instant
)

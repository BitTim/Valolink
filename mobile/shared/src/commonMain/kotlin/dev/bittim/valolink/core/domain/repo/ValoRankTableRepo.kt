/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoRankTableRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:13
 */

package dev.bittim.valolink.core.domain.repo

interface ValoRankTableRepo {
    /**
 * Synchronizes the ValoRank table data.
 */
suspend fun sync()
}
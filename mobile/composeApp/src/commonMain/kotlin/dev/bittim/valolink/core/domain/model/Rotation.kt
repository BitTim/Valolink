/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       Rotation.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.05.26, 01:00
 */

package dev.bittim.valolink.core.domain.model

data class Rotation(
    val pitch: Float,
    val yaw: Float,
    val roll: Float
)

/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContentContainerState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 20:32
 */

package dev.bittim.valolink.content.ui.container

import android.graphics.Bitmap

data class ContentContainerState(
    val isAuthenticated: Boolean? = null,
    val hasOnboarded: Boolean? = null,
    val userAvatar: Bitmap? = null,
)

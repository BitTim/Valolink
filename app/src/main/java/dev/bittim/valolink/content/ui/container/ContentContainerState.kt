/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContentContainerState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:17
 */

package dev.bittim.valolink.content.ui.container

data class ContentContainerState(
    val isAuthenticated: Boolean? = null,
    val hasOnboarded: Boolean? = null,
)

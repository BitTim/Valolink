/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       HomeState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:30
 */

package dev.bittim.valolink.content.ui.screens.content.home

data class HomeState(
    val isLoading: Boolean = false,
    val username: String? = null,
)

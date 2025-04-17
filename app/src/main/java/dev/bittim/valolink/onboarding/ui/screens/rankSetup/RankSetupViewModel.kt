/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankSetupViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   17.04.25, 03:37
 */

package dev.bittim.valolink.onboarding.ui.screens.rankSetup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class RankSetupViewModel @Inject constructor(

) : ViewModel() {
    private val _state = MutableStateFlow(RankSetupState())
    val state = _state.asStateFlow()
}

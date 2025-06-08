/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       FriendsViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 20:32
 */

package dev.bittim.valolink.content.ui.screens.content.friends

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.user.data.repository.data.UserMetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val userMetaRepository: UserMetaRepository
) : ViewModel() {
    private var _state = MutableStateFlow(FriendsState())
    val state = _state.asStateFlow()

    fun onFetch() {
        _state.update { it.copy(isLoading = true) }
    }
}

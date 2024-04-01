package dev.bittim.valolink.feature.content.ui.main

sealed interface MainState {
    data object NoAuth : MainState
    data object Loading : MainState
    data object Content : MainState
}
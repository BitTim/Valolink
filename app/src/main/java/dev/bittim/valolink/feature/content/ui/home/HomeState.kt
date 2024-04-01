package dev.bittim.valolink.feature.content.ui.home

sealed interface HomeState {
    data object Fetching : HomeState
    data object Loading : HomeState
    data class Content(
        val username: String
    ) : HomeState
}
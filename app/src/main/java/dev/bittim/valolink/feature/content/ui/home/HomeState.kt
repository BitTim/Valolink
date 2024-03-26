package dev.bittim.valolink.feature.content.ui.home

sealed interface HomeState {
    data object NotAuthorized : HomeState
    data object Loading : HomeState
    data class Content(
        val username: String
    ) : HomeState
}
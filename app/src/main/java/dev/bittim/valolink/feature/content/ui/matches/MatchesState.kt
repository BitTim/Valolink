package dev.bittim.valolink.feature.content.ui.matches

sealed interface MatchesState {
    data object Fetching : MatchesState
    data object Loading : MatchesState
    data object Content : MatchesState
}
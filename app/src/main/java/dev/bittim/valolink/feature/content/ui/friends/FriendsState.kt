package dev.bittim.valolink.feature.content.ui.friends

sealed interface FriendsState {
    data object Fetching : FriendsState
    data object Loading : FriendsState
    data object Content : FriendsState
}
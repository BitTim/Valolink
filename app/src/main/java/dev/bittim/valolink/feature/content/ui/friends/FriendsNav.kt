package dev.bittim.valolink.feature.content.ui.friends

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val FriendsNavRoute = "friends"

fun NavGraphBuilder.friendsScreen() {
    composable(FriendsNavRoute) {
        val viewModel: FriendsViewModel = hiltViewModel()
        val friendsState by viewModel.friendsState.collectAsStateWithLifecycle()

        FriendsScreen(
            state = friendsState,
            onFetch = viewModel::onFetch
        )
    }
}

fun NavController.navToFriends(origin: String) {
    navigate(FriendsNavRoute) {
        popUpTo(origin) {
            inclusive = true
        }
        launchSingleTop = true
    }
}
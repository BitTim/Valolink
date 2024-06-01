package dev.bittim.valolink.main.ui.nav.content

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bittim.valolink.core.ui.theme.Transition
import dev.bittim.valolink.main.ui.screens.content.friends.FriendsScreen
import dev.bittim.valolink.main.ui.screens.content.friends.FriendsViewModel
import kotlinx.serialization.Serializable

@Serializable
object FriendsNav

fun NavGraphBuilder.friendsScreen() {
    composable<FriendsNav>(enterTransition = { Transition.topLevelEnter },
                           exitTransition = { Transition.topLevelExit },
                           popEnterTransition = { Transition.topLevelEnter },
                           popExitTransition = { Transition.topLevelExit }) {
        val viewModel: FriendsViewModel = hiltViewModel()
        val friendsState by viewModel.friendsState.collectAsStateWithLifecycle()

        FriendsScreen(
            state = friendsState,
            onFetch = viewModel::onFetch
        )
    }
}

fun NavController.navToFriends() {
    navigate(FriendsNav) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
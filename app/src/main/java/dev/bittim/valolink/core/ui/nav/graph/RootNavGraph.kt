package dev.bittim.valolink.core.ui.nav.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bittim.valolink.core.ui.asLifecycleAwareState
import dev.bittim.valolink.core.ui.nav.Routes
import dev.bittim.valolink.core.ui.nav.navigator.Navigator

@Composable
fun RootNavGraph(
    navController: NavHostController,
    navigator: Navigator
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val navigatorState by navigator.navActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )

    LaunchedEffect(navigatorState) {
        navigatorState?.let {
            it.parcelableArguments.forEach { arg ->
                navController.currentBackStackEntry?.arguments?.putParcelable(arg.key, arg.value)
            }
            navController.navigate(it.route, it.navOptions)
        }
    }

    NavHost(
        navController = navController,
        route = Routes.Root.route,
        startDestination = Routes.Auth.route
    ) {
        authNavGraph()
    }
}
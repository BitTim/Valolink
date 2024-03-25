package dev.bittim.valolink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.bittim.valolink.core.ui.nav.graph.RootNavGraph
import dev.bittim.valolink.core.ui.nav.navigator.Navigator
import dev.bittim.valolink.ui.theme.ValolinkTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor(
    private val navigator: Navigator
) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ValolinkTheme {
                RootNavGraph(navController = rememberNavController(), navigator)
            }
        }
    }
}
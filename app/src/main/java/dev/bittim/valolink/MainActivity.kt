package dev.bittim.valolink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.bittim.valolink.navgraph.RootNavGraph
import dev.bittim.valolink.ui.theme.ValolinkTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ValolinkTheme {
                RootNavGraph(navController = rememberNavController())
            }
        }
    }
}
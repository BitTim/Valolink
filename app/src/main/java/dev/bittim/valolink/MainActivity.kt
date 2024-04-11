package dev.bittim.valolink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.bittim.valolink.core.ui.RootNavGraph
import dev.bittim.valolink.ui.theme.ValolinkTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            ValolinkTheme {
                Surface {
                    RootNavGraph(
                        modifier = Modifier.fillMaxSize(),
                        navController = rememberNavController()
                    )
                }
            }
        }
    }
}
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
import dev.bittim.valolink.core.ui.nav.RootNavGraph
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.handleDeeplinks
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var supabase: SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supabase.handleDeeplinks(intent)
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
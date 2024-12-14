/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       MainActivity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:30
 */

package dev.bittim.valolink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.bittim.valolink.core.ui.RootNavGraph
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var supabase: SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        supabase.handleDeeplinks(intent)
        setDecorFitsSystemWindows(window, false)
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
/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       HomeScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 20:32
 */

package dev.bittim.valolink.content.ui.screens.content.home

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.content.ui.components.ValolinkTopAppBar
import dev.bittim.valolink.core.ui.theme.ValolinkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    userAvatar: Bitmap?,
    signOut: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ValolinkTopAppBar(userAvatar, scrollBehavior)

        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp
            )
        ) {
            Text(
                text = "Welcome back, ${state.username}",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Button(onClick = signOut) {
                Text(text = "Sign out")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ValolinkTheme {
        HomeScreen(
            state = HomeState(username = "John Doe"),
            userAvatar = null,
            signOut = {}
        )
    }
}

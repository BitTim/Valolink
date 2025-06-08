/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       FriendsScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 20:32
 */

package dev.bittim.valolink.content.ui.screens.content.friends

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
fun FriendsScreen(
    state: FriendsState,
    userAvatar: Bitmap?,
    onFetch: () -> Unit,
) {
    // TODO: Placeholder
    if (!state.isLoading) {
        onFetch()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        ValolinkTopAppBar(userAvatar, scrollBehavior)

        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp
            )
        ) {
            Text(
                text = "Friends",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun FriendsScreenPreview() {
    ValolinkTheme {
        FriendsScreen(
            state = FriendsState(),
            userAvatar = null,
            onFetch = {}
        )
    }
}

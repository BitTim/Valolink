/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MatchesOverviewScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.ui.screens.content.matches.overview

import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import dev.bittim.valolink.R
import dev.bittim.valolink.content.ui.components.ValolinkTopAppBar
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MatchesScreen(
    state: MatchesOverviewState,
    userAvatar: Bitmap?,
    onFetch: () -> Unit,
) {
    // TODO: Placeholder
    if (!state.isLoading) {
        onFetch()
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = { ValolinkTopAppBar(userAvatar, scrollBehavior) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) {
        Crossfade(
            modifier = Modifier.padding(it),
            targetState = state.matchGroups
        ) {
            if (it.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Spacing.xxxl),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = UiText.StringResource(R.string.matches_overview_empty_title)
                            .asString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(Spacing.xs))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = UiText.StringResource(R.string.matches_overview_empty_description)
                            .asString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = Spacing.l,
                            top = Spacing.l,
                            end = Spacing.l
                        )
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    verticalArrangement = Arrangement.spacedBy(Spacing.m)
                ) { }
            }
        }
    }
}

@ScreenPreviewAnnotations
@Composable
fun MatchesScreenPreview() {
    ValolinkTheme {
        MatchesScreen(
            state = MatchesOverviewState(),
            userAvatar = null,
            onFetch = {}
        )
    }
}

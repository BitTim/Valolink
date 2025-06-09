/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ValolinkTopAppBar.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.util.UiText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ValolinkTopAppBar(
    userAvatar: Bitmap?,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { Text(text = UiText.StringResource(R.string.app_name).asString()) },
        actions = {
            IconButton(modifier = Modifier.padding(horizontal = Spacing.s), onClick = { }) {
                if (userAvatar == null) {
                    LoadingIndicator()
                } else {
                    AsyncImage(
                        modifier = Modifier
                            .size(Spacing.xxxl)
                            .aspectRatio(1f)
                            .clip(CircleShape),
                        model = userAvatar,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_displayicon),
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        windowInsets = WindowInsets.statusBars
    )
}
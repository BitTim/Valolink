/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       OnboardingScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:32
 */

package dev.bittim.valolink.onboarding.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.bittim.valolink.core.ui.components.OrientableContainer
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    title: String,
    progress: Float,
    description: String,
    content: @Composable () -> Unit,
    form: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .safeContentPadding(),
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        OnboardingHeader(
            modifier = Modifier.fillMaxWidth(),
            title = title,
            progress = progress,
            description = description
        )

        OrientableContainer(
            modifier = Modifier.fillMaxSize(),
            portraitContainer = { containerModifier, containerContent ->
                Column(
                    modifier = containerModifier,
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) { containerContent() }
            },
            landscapeContainer = { containerModifier, containerContent ->
                Row(
                    modifier = containerModifier,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) { containerContent() }
            }
        ) {

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomStart
            ) { content() }

            Spacer(
                modifier = Modifier.size(Spacing.m)
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomStart
            ) { form() }
        }
    }
}

@ScreenPreviewAnnotations
@Composable
fun OnboardingScreenPreview() {
    ValolinkTheme {
        Surface {
            OnboardingScreen(
                modifier = Modifier.fillMaxSize(),
                title = "Sample section",
                progress = 0.37f,
                description = "This is a sample section that is supposed to serve as a preview to this composable. If this text is visible, then this sample is working fine. If you see this in the finished app, something went wrong... Like really wrong",
                content = {
                    Box(
                        Modifier
                            .background(Color.Red)
                            .fillMaxSize()
                    )
                },
                form = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(onClick = {}) { Text("Back") }
                        Button(onClick = {}) { Text("Continue") }
                    }
                }
            )
        }
    }
}
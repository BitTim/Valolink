/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       FinishScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   02.05.25, 08:26
 */

package dev.bittim.valolink.onboarding.ui.screens.finish

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.core.ui.components.SimpleLoadingContainer
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations
import dev.bittim.valolink.onboarding.ui.components.OnboardingButtons
import dev.bittim.valolink.onboarding.ui.components.OnboardingLayout

data object FinishScreen {
    const val SPRAY_UUID: String = "22ae8535-40b6-95a3-c5b1-98a184b8909f"
}

@Composable
fun FinishScreen(
    state: FinishState,
    navBack: () -> Unit,
    finish: () -> Unit,
) {
    OnboardingLayout(
        modifier = Modifier
            .fillMaxSize(),
        content = {
            SimpleLoadingContainer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing.l),
                isLoading = state.loading,
                label = "Spray image loading crossfade"
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Spacing.l),
                    model = state.spray?.animationPng,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_displayicon),
                )
            }
        },
        form = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.l),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                OnboardingButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onDismiss = navBack,
                    onContinue = finish,
                    disableContinueButton = false,
                    dismissText = UiText.StringResource(R.string.button_back),
                    continueText = UiText.StringResource(R.string.onboarding_finish_button_finish)
                )
            }
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@ScreenPreviewAnnotations
@Composable
fun LandingScreenPreview() {
    ValolinkTheme {
        Surface {
            FinishScreen(
                state = FinishState(),
                navBack = {},
                finish = {}
            )
        }
    }
}

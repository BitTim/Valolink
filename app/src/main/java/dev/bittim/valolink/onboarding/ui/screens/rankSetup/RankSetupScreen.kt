/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankSetupScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   17.04.25, 03:37
 */

package dev.bittim.valolink.onboarding.ui.screens.rankSetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.components.LabeledSlider
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations
import dev.bittim.valolink.onboarding.ui.components.OnboardingButtons
import dev.bittim.valolink.onboarding.ui.components.OnboardingLayout

@Composable
fun RankSetupScreen(
    state: RankSetupState,
    navBack: () -> Unit,
) {
    OnboardingLayout(
        content = { },
        form = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                LabeledSlider(
                    label = UiText.StringResource(R.string.onboarding_rankSetup_slider_currentRank_label)
                        .asString(),
                    initialValue = 0f,
                    valueRange = 0f..23f,
                    isStepped = true,
                    showLimitLabels = false,
                    onValueChange = {}
                )

                LabeledSlider(
                    label = UiText.StringResource(R.string.onboarding_rankSetup_slider_currentRR_label)
                        .asString(),
                    initialValue = 0f,
                    valueRange = 0f..99f,
                    isStepped = false,
                    showLimitLabels = true,
                    onValueChange = {}
                )

                Spacer(modifier = Modifier.weight(1f))

                OnboardingButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onDismiss = navBack,
                    onContinue = { },
                    disableContinueButton = state.isLoading,
                    dismissText = UiText.StringResource(R.string.button_back),
                    continueText = UiText.StringResource(R.string.button_continue)
                )
            }
        }
    )
}

@ScreenPreviewAnnotations
@Composable
fun RankSetupScreenPreview() {
    ValolinkTheme {
        Surface {
            RankSetupScreen(
                RankSetupState(),
                navBack = {}
            )
        }
    }
}

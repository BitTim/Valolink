/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthFlowScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   01.06.26, 19:26
 */

package dev.bittim.valolink.feature.auth.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.core.ui.components.AnimatedHeroIcon
import dev.bittim.valolink.core.ui.components.flowScaffold.FlowScaffold
import dev.bittim.valolink.feature.auth.ui.screen.steps.LandingStep
import dev.bittim.valolink.feature.auth.ui.screen.steps.OtpStep
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.iconcd_email
import valolink.shared.generated.resources.iconcd_waving_hand

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
@Preview
fun AuthFlowScreen(
    state: AuthFlowState = AuthFlowState(),
    onAction: (action: AuthFlowAction) -> Unit = {},
    navBack: () -> Unit = {},
) {
    FlowScaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.displayCutout)
            .windowInsetsPadding(WindowInsets.systemBars)
            .windowInsetsPadding(WindowInsets.ime),
        padding = PaddingValues(horizontal = Spacing.l),
        step = state.step,
        cancellable = false,
        onBack = { onAction(AuthFlowAction.Back(navBack)) },
        hero = {
            AnimatedHeroIcon(
                shape = MaterialShapes.Cookie12Sided.toShape(),
                icon = when(state.step) {
                    AuthFlowStep.LandingStep -> Icons.Default.WavingHand
                    AuthFlowStep.OtpStep -> Icons.Default.Email
                },
                contentDescription = when(state.step) {
                    AuthFlowStep.LandingStep -> stringResource(Res.string.iconcd_waving_hand)
                    AuthFlowStep.OtpStep -> stringResource(Res.string.iconcd_email)
                },
                targetRotation = state.heroIconRotation
            )
        },
        content = { _, padding ->
            when(state.step) {
                AuthFlowStep.LandingStep -> LandingStep(
                    modifier = Modifier.padding(padding),
                    email = state.email,
                    error = state.emailError,
                    onAction = { onAction(it) }
                )
                AuthFlowStep.OtpStep -> OtpStep(
                    modifier = Modifier.padding(padding),
                    email = state.email,
                    otp = state.otp,
                    error = state.otpError,
                    cooldownSecondsLeft = state.cooldownSecondsLeft,
                    onAction = { onAction(it) }
                )
            }
        },
    )
}
/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       LandingScreen.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 13:00
 */

package dev.bittim.valolink.feature.onboarding.ui.screen.landing

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.core.ui.Spacing
import dev.bittim.valolink.feature.onboarding.ui.screen.landing.components.OutlinedSocialButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.*

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
@Preview
fun LandingScreen(
    navHome: () -> Unit = {}
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(Spacing.l),
            verticalArrangement = Arrangement.spacedBy(Spacing.l)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .weight(1f)
            ) {}

            Column(
                modifier = Modifier.fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.m)
            ) {
                Text(
                    text = stringResource(Res.string.onboarding_landing_title),
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(text = stringResource(Res.string.onboarding_landing_description))

                Column() {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = navHome,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.s)
                        ) {
                            Icon(
                                Icons.Outlined.Email,
                                contentDescription = "",
                            )

                            Text(text = stringResource(Res.string.onboarding_landing_button_email))
                        }
                    }

                    OutlinedSocialButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {},
                        painter = painterResource(Res.drawable.ic_google_logo),
                        contentDescription = "Google",
                        text = stringResource(Res.string.onboarding_landing_button_google)
                    )
                }
            }
        }
    }
}
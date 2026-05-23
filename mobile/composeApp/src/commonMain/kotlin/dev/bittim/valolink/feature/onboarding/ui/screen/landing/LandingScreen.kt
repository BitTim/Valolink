/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       LandingScreen.kt
 * Module:     Valolink.composeApp.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 13:36
 */

package dev.bittim.valolink.feature.onboarding.ui.screen.landing

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
@Preview
fun LandingScreen() {
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .weight(1f)
        ) {}

        Column (
            modifier = Modifier.fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Welcome to Valolink!",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(text = "Start your journey with Valolink by signing up or logging in")

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
            ) {
                Row() {
                    Icon(
                        Icons.Outlined.Email,
                        contentDescription = "",
                    )

                    Text(text = "Continue with E-Mail")
                }
            }
        }
    }
}
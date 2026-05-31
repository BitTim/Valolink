/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FlowScaffold.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.05.26, 21:43
 */

package dev.bittim.valolink.core.ui.components.flowScaffold

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import dev.bittim.valolink.core.ui.Spacing
import org.jetbrains.compose.resources.stringResource
import valolink.shared.generated.resources.Res
import valolink.shared.generated.resources.iconcd_back
import valolink.shared.generated.resources.iconcd_close

@Composable
fun <S: FlowStep> FlowScaffold(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(horizontal = Spacing.l),
    step: S,
    cancellable: Boolean = true,
    onBack: () -> Unit = {},
    hero: @Composable () -> Unit = {},
    content: @Composable AnimatedContentScope.(S, padding: PaddingValues) -> Unit
) {
    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        onBackCompleted = onBack
    )

    Surface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing.l)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(padding),
            ) {
                AnimatedContent(
                    modifier = Modifier.align(Alignment.CenterStart),
                    targetState = step.index == 0
                ) {
                    if (it) {
                        IconButton(
                            enabled = cancellable,
                            onClick = onBack
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(Res.string.iconcd_close)
                            )
                        }
                    } else {
                        IconButton(
                            onClick = onBack
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(Res.string.iconcd_back)
                            )
                        }
                    }
                }

                LinearProgressIndicator(
                    modifier = Modifier.height(Spacing.s)
                        .fillMaxWidth(0.5f)
                        .align(Alignment.Center),
                    progress = { step.progress }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                Box(
                    modifier = Modifier.weight(0.25f)
                        .padding(padding)
                        .align(Alignment.CenterHorizontally)
                ) {
                    hero()
                }

                AnimatedContent(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f),
                    targetState = step,
                    transitionSpec = {
                        if (targetState.index > initialState.index) {
                            slideInHorizontally { it } + fadeIn() togetherWith
                                    slideOutHorizontally { -it } + fadeOut()
                        } else {
                            slideInHorizontally { -it } + fadeIn() togetherWith
                                    slideOutHorizontally { it } + fadeOut()
                        }
                    },
                ) { currentStep ->
                    content(currentStep, padding)
                }
            }
        }
    }
}

class PreviewStep(
    override val progress: Float,
    override val index: Int
) : FlowStep

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun FlowScaffoldPreview() {
    MaterialTheme {
        FlowScaffold(
            modifier = Modifier.fillMaxSize(),
            step = PreviewStep(0f, 0),
            cancellable = false,
            hero = {
                Box(
                    modifier = Modifier.aspectRatio(1f)
                        .padding(Spacing.l)
                        .clip(MaterialShapes.Cookie12Sided.toShape())
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize().padding(Spacing.xxl),
                        imageVector = Icons.Default.Android,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = null
                    )
                }
            },
            content = { _, padding ->
                Column(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s)
                ) {
                    Text(
                        text = "Sample Title",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "This is a sample description. It is not used in any actual screen and only visible in this preview.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {}
                    ) {
                        Text(text = "Sample Button")
                    }
                }
            }
        )
    }
}
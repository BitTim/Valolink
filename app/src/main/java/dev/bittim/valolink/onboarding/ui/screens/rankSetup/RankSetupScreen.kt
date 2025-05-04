/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankSetupScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   04.05.25, 14:03
 */

package dev.bittim.valolink.onboarding.ui.screens.rankSetup

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.components.LabeledSlider
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ComponentPreviewAnnotations
import dev.bittim.valolink.core.ui.util.extensions.modifier.SATURATION_DESATURATED
import dev.bittim.valolink.core.ui.util.extensions.modifier.saturation
import dev.bittim.valolink.onboarding.ui.components.OnboardingButtons
import dev.bittim.valolink.onboarding.ui.components.OnboardingLayout
import dev.bittim.valolink.onboarding.ui.screens.rankSetup.components.RankCard
import dev.bittim.valolink.onboarding.ui.screens.rankSetup.components.RankCardData
import kotlin.math.absoluteValue

const val MAX_RR = 99

@Composable
fun RankSetupScreen(
    state: RankSetupState,
    navBack: () -> Unit,
    onTierChanged: (tier: Int) -> Unit,
    onRRChanged: (tier: Int) -> Unit,
    onMatchesPlayedChanged: (tier: Int) -> Unit,
    onMatchesNeededChanged: (tier: Int) -> Unit,
    setRank: () -> Unit,
) {
    var clickedPage by remember { mutableIntStateOf(0) }
    var pagerState = rememberPagerState(pageCount = { state.ranks?.count() ?: 0 })
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    val targetPage by remember { derivedStateOf { pagerState.targetPage } }

    var isUnranked by remember { mutableStateOf(true) }
    var isHighest by remember { mutableStateOf(false) }

    var rrString by remember { mutableStateOf(state.rr.toString()) }
    var rrError: UiText? by remember { mutableStateOf(null) }

    LaunchedEffect(currentPage, targetPage) {
        if (currentPage == targetPage) {
            val newTier = state.ranks?.getOrNull(currentPage)?.tier ?: 0
            if (newTier != state.tier) onTierChanged(newTier)
        }
    }

    LaunchedEffect(state.tier) {
        val newPage = state.ranks?.indexOfFirst { it.tier == state.tier } ?: 0
        pagerState.animateScrollToPage(newPage)

        isUnranked = state.tier == 0
        isHighest = state.tier == state.ranks?.last()?.tier

        if (!isHighest && state.rr > MAX_RR) {
            onRRChanged(MAX_RR)
        }
    }

    LaunchedEffect(state.rr) {
        val newRRString = state.rr.toString()
        if (newRRString != rrString) {
            rrString = newRRString
        }
    }

    LaunchedEffect(clickedPage) {
        pagerState.animateScrollToPage(clickedPage)
    }

    OnboardingLayout(
        modifier = Modifier
            .fillMaxSize(),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing.l),
                contentAlignment = Alignment.Center
            ) {
                val pageSize = RankCard.height + (Spacing.xs * 2)

                VerticalPager(
                    modifier = Modifier
                        .height(pageSize * 5)
                        .align(Alignment.Center),
                    state = pagerState,
                    pageSize = PageSize.Fixed(pageSize),
                    contentPadding = PaddingValues(vertical = pageSize * 2),
                    flingBehavior = PagerDefaults.flingBehavior(
                        state = pagerState,
                        pagerSnapDistance = PagerSnapDistance.atMost(pagerState.pageCount)
                    ),
                ) {
                    val rank = state.ranks?.getOrNull(it)
                    val rankCardData = if (rank == null) null else {
                        RankCardData(
                            rankName = rank.name,
                            rankIcon = rank.icon,
                            gradient = listOf(
                                rank.color,
                                rank.backgroundColor
                            )
                        )
                    }

                    val pageOffset = pagerState.getOffsetDistanceInPages(it).absoluteValue
                    val horizontalPadding = (32f * minOf(pageOffset, 1f)).dp
                    val blur =
                        lerp(
                            start = 0f,
                            stop = 4f,
                            fraction = minOf(pageOffset, 1f).coerceIn(0f, 1f)
                        ).dp
                    val saturation = lerp(
                        start = SATURATION_DESATURATED,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                    RankCard(
                        modifier = Modifier
                            .blur(blur)
                            .padding(horizontalPadding, Spacing.xs)
                            .saturation(saturation)
                            .graphicsLayer {
                                alpha = lerp(
                                    start = 0f,
                                    stop = 1f,
                                    fraction = 2f - pageOffset.coerceIn(0f, 2f)
                                )
                            }
                            .clickable {
                                if (pageOffset <= 1) clickedPage = it
                            },
                        data = rankCardData,
                        isUnranked = rank?.tier == 0,
                        rr = if ((rank?.tier
                                ?: 0) < state.tier
                        ) MAX_RR else if (rank?.tier == state.tier) state.rr else 0,
                        deltaRR = 0,
                        matchesPlayed = state.matchesPlayed,
                        matchesNeeded = state.matchesNeeded
                    )
                }
            }
        },
        form = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.l),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                AnimatedContent(
                    modifier = Modifier.wrapContentHeight(),
                    targetState = isUnranked,
                    label = "Unranked crossfade"
                ) { isUnranked ->
                    if (isUnranked) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(Spacing.s)
                        ) {
                            LabeledSlider(
                                label = UiText.StringResource(R.string.onboarding_rankSetup_slider_matchesPlayed_label)
                                    .asString(),
                                initialValue = state.matchesPlayed.toFloat(),
                                valueRange = 0f..state.matchesNeeded.toFloat(),
                                isStepped = false,
                                showLimitLabels = true,
                                onValueChange = {
                                    onMatchesPlayedChanged(it.toInt())
                                }
                            )

                            LabeledSlider(
                                label = UiText.StringResource(R.string.onboarding_rankSetup_slider_matchesNeeded_label)
                                    .asString(),
                                initialValue = state.matchesNeeded.toFloat(),
                                valueRange = 1f..5f,
                                isStepped = false,
                                showLimitLabels = true,
                                onValueChange = {
                                    onMatchesNeededChanged(it.toInt())
                                }
                            )
                        }
                    } else {
                        AnimatedContent(
                            modifier = Modifier.wrapContentHeight(),
                            targetState = isHighest,
                            label = "Is Highest crossfade"
                        ) { isHighest ->
                            if (!isHighest) {
                                LabeledSlider(
                                    label = UiText.StringResource(R.string.onboarding_rankSetup_slider_currentRR_label)
                                        .asString(),
                                    initialValue = state.rr.toFloat(),
                                    valueRange = 0f..MAX_RR.toFloat(),
                                    isStepped = false,
                                    showLimitLabels = true,
                                    onValueChange = {
                                        onRRChanged(it.toInt())
                                    }
                                )
                            } else {
                                OutlinedTextFieldWithError(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = UiText.StringResource(R.string.onboarding_rankSetup_slider_currentRR_label)
                                        .asString(),
                                    value = rrString,
                                    error = rrError,
                                    onValueChange = {
                                        rrString = it

                                        try {
                                            onRRChanged(rrString.toInt())
                                        } catch (_: NumberFormatException) {
                                            rrError =
                                                UiText.StringResource(R.string.error_numberFormat)
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xl))

                OnboardingButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onDismiss = navBack,
                    onContinue = setRank,
                    disableContinueButton = state.ranks == null,
                    dismissText = UiText.StringResource(R.string.button_back),
                    continueText = UiText.StringResource(R.string.button_continue)
                )
            }
        }
    )
}

@ComponentPreviewAnnotations
@Composable
fun RankSetupScreenPreview() {
    ValolinkTheme {
        Surface {
            RankSetupScreen(
                RankSetupState(),
                navBack = {},
                setRank = {},
                onTierChanged = {},
                onRRChanged = {},
                onMatchesPlayedChanged = {},
                onMatchesNeededChanged = {}
            )
        }
    }
}

/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankSetupScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.04.25, 17:24
 */

package dev.bittim.valolink.onboarding.ui.screens.rankSetup

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
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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

@Composable
fun RankSetupScreen(
    state: RankSetupState,
    signOut: () -> Unit,
    setRank: (tier: Int, rr: Int, matchesPlayed: Int, matchesNeeded: Int) -> Unit,
) {
    var rr by remember { mutableIntStateOf(50) }
    var matchesPlayed by remember { mutableIntStateOf(0) }
    var matchesNeeded by remember { mutableIntStateOf(5) }

    val pagerState = rememberPagerState(pageCount = { state.ranks?.count() ?: 0 })
    var clickedPage by remember { mutableIntStateOf(0) }

    var isUnranked by remember { mutableStateOf(true) }
    var isHighest by remember { mutableStateOf(false) }
    var page by remember { mutableIntStateOf(0) }

    var rrString by remember { mutableStateOf(rr.toString()) }
    var rrError by remember { mutableStateOf<UiText?>(null) }

    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow { pagerState.currentPage }.collect {
            isUnranked = (state.ranks?.get(it)?.tier ?: 0) == 0
            isHighest = it == state.ranks?.lastIndex
            page = it

            if (!isHighest && rr > 99) {
                rr = 99
            }
        }
    }

    LaunchedEffect(rr) {
        rrString = rr.toString()
    }

    LaunchedEffect(clickedPage) {
        pagerState.animateScrollToPage(clickedPage)
    }

    OnboardingLayout(
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                VerticalPager(
                    modifier = Modifier.height(RankCard.height * 3),
                    state = pagerState,
                    pageSize = PageSize.Fixed(RankCard.height),
                    contentPadding = PaddingValues(vertical = RankCard.height),
                    flingBehavior = PagerDefaults.flingBehavior(
                        state = pagerState,
                        pagerSnapDistance = PagerSnapDistance.atMost(10)
                    ),
                ) {
                    val rank = state.ranks?.get(it)
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
                    val horizontalPadding = (32f * pageOffset).dp
                    val blur =
                        lerp(start = 0f, stop = 4f, fraction = pageOffset.coerceIn(0f, 1f)).dp
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
                                clickedPage = it
                            },
                        data = rankCardData,
                        isUnranked = rank?.tier == 0,
                        rr = if (it < page) 99 else if (it == page) rr else 0,
                        deltaRR = 0,
                        matchesPlayed = matchesPlayed,
                        matchesNeeded = matchesNeeded
                    )
                }
            }
        },
        form = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                if (isUnranked) {
                    LabeledSlider(
                        label = UiText.StringResource(R.string.onboarding_rankSetup_slider_matchesPlayed_label)
                            .asString(),
                        initialValue = matchesPlayed.toFloat(),
                        valueRange = 0f..matchesNeeded.toFloat(),
                        isStepped = false,
                        showLimitLabels = true,
                        onValueChange = {
                            matchesPlayed = it.toInt()
                        }
                    )

                    LabeledSlider(
                        label = UiText.StringResource(R.string.onboarding_rankSetup_slider_matchesNeeded_label)
                            .asString(),
                        initialValue = matchesNeeded.toFloat(),
                        valueRange = 1f..5f,
                        isStepped = false,
                        showLimitLabels = true,
                        onValueChange = {
                            matchesNeeded = it.toInt()
                            if (matchesPlayed > matchesNeeded) {
                                matchesPlayed = matchesNeeded
                            }
                        }
                    )
                } else {
                    if (!isHighest) {
                        LabeledSlider(
                            label = UiText.StringResource(R.string.onboarding_rankSetup_slider_currentRR_label)
                                .asString(),
                            initialValue = rr.toFloat(),
                            valueRange = 0f..99f,
                            isStepped = false,
                            showLimitLabels = true,
                            onValueChange = {
                                rr = it.toInt()
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
                                    rr = rrString.toInt()
                                    rrError = null
                                } catch (e: NumberFormatException) {
                                    rrError = UiText.DynamicString(
                                        e.localizedMessage ?: e.message ?: "Invalid number"
                                    )
                                }
                            },
                            visibility = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                OnboardingButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onDismiss = signOut,
                    onContinue = {
                        setRank(
                            state.ranks!![page].tier,
                            rr,
                            matchesPlayed,
                            matchesNeeded
                        )
                    },
                    disableContinueButton = state.ranks == null,
                    dismissText = UiText.StringResource(R.string.button_cancel),
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
                signOut = {},
                setRank = { _, _, _, _ -> }
            )
        }
    }
}

/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractSetupScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 19:57
 */

package dev.bittim.valolink.onboarding.ui.screens.contractSetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.core.ui.components.rewardCard.RewardCard
import dev.bittim.valolink.core.ui.components.rewardCard.RewardCardData
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations
import dev.bittim.valolink.core.ui.util.extensions.modifier.SATURATION_DESATURATED
import dev.bittim.valolink.core.ui.util.extensions.modifier.saturation
import dev.bittim.valolink.onboarding.ui.components.OnboardingButtons
import dev.bittim.valolink.onboarding.ui.components.OnboardingLayout
import kotlin.math.absoluteValue

@Composable
fun ContractSetupScreen(
    state: ContractSetupState
) {
    val levels = state.contract?.content?.chapters?.flatMap { it.levels }?.drop(1)
    val pagerState = rememberPagerState(pageCount = { levels?.count() ?: 0 })
    var clickedPage by remember { mutableIntStateOf(0) }
    var page by remember { mutableIntStateOf(0) }
    var isHighest by remember { mutableStateOf(false) }

    var xpTotal by remember { mutableIntStateOf(0) }
    var xpCollected by remember { mutableIntStateOf(0) }
    var xpCollectedString by remember { mutableStateOf(xpCollected.toString()) }
    var xpCollectedError by remember { mutableStateOf<UiText?>(null) }

    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow { pagerState.currentPage }.collect {
            page = it
            isHighest = it == levels?.lastIndex
            xpTotal = levels?.get(page)?.xp ?: 0

            if (!isHighest && xpCollected > xpTotal) {
                xpCollectedString = xpTotal.toString()
            }
        }
    }

    LaunchedEffect(xpCollectedString) {
        try {
            val uncheckedXpCollected = xpCollectedString.toInt()
            if (uncheckedXpCollected > xpTotal && !isHighest) {
                xpCollected = xpTotal
                xpCollectedError =
                    UiText.StringResource(R.string.onboarding_contractSetup_xpCollected_error)
            } else {
                xpCollected = uncheckedXpCollected
                xpCollectedError = null
            }
        } catch (e: NumberFormatException) {
            xpCollectedError =
                UiText.StringResource(R.string.error_numberFormat)
        }
    }

    LaunchedEffect(clickedPage) {
        pagerState.animateScrollToPage(clickedPage)
    }

    OnboardingLayout(
        modifier = Modifier.fillMaxSize(),
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val contentPadding =
                    (LocalConfiguration.current.screenWidthDp.dp - RewardCard.width) / 2

                HorizontalPager(
                    state = pagerState,
                    pageSize = PageSize.Fixed(RewardCard.width),
                    contentPadding = PaddingValues(horizontal = contentPadding),
                    flingBehavior = PagerDefaults.flingBehavior(
                        state = pagerState,
                        pagerSnapDistance = PagerSnapDistance.atMost(pagerState.pageCount)
                    ),
                ) { thisPage ->
                    val level = levels?.get(thisPage)
                    val reward = level?.rewards?.find { !it.isFreeReward }?.relation

                    val pageOffset = pagerState.getOffsetDistanceInPages(thisPage).absoluteValue
                    val topPadding = (32f * minOf(pageOffset, 1f)).dp
                    val bottomPadding = 32.dp - topPadding
                    val blur =
                        lerp(start = 0f, stop = 4f, fraction = pageOffset.coerceIn(0f, 1f)).dp
                    val saturation = lerp(
                        start = SATURATION_DESATURATED,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                    RewardCard(
                        modifier = Modifier
                            .blur(blur)
                            .padding(
                                start = Spacing.xs,
                                end = Spacing.xs,
                                top = Spacing.xs + topPadding,
                                bottom = Spacing.xs + bottomPadding
                            )
                            .saturation(saturation)
                            .graphicsLayer {
                                alpha = lerp(
                                    start = 0f,
                                    stop = 1f,
                                    fraction = 2f - pageOffset.coerceIn(0f, 2f)
                                )
                            },
                        data = if (reward == null) null else RewardCardData(
                            name = reward.displayName,
                            levelUuid = level.uuid,
                            type = reward.type,
                            levelName = level.name,
                            contractName = state.contract.displayName,
                            rewardCount = level.rewards.count(),
                            previewIcon = reward.previewImages.first().first ?: "",
                            background = reward.background,
                            useXP = true,
                            xpTotal = level.xp,
                            price = level.vpCost,
                            amount = reward.amount,
                            currencyIcon = "",
                        ),
                        xpCollected = if (thisPage < page) level?.xp
                            ?: 0 else if (thisPage == page) xpCollected else 0,
                        isOwned = thisPage < page,
                        onNavToLevelDetails = {
                            clickedPage = thisPage
                        },
                    )
                }
            }
        },
        form = {
            Column(
                modifier = Modifier
                    .padding(horizontal = Spacing.l),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                OutlinedTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    label = UiText.StringResource(R.string.onboarding_contractSetup_xpCollected_label)
                        .asString(),
                    value = xpCollectedString,
                    error = xpCollectedError,
                    onValueChange = {
                        xpCollectedString = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                )

                Spacer(modifier = Modifier.height(Spacing.xl))

                OnboardingButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onDismiss = {},
                    onContinue = { },
                    disableContinueButton = xpCollectedError != null || levels != null,
                    dismissText = UiText.StringResource(R.string.button_cancel),
                    continueText = UiText.StringResource(R.string.onboarding_createAccount_button_createAccount)
                )
            }
        }
    )
}

@ScreenPreviewAnnotations
@Composable
fun ContractSetupScreenPreview() {
    ValolinkTheme {
        Surface {
            ContractSetupScreen(state = ContractSetupState())
        }
    }
}

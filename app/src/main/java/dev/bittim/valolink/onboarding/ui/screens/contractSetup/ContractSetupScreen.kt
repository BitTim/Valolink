/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractSetupScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.onboarding.ui.screens.contractSetup

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.components.LabeledSwitch
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
import dev.bittim.valolink.onboarding.ui.screens.contractSetup.dialogs.FreeOnlyInfoDialog
import kotlin.math.absoluteValue

const val PAGE_OFFSET = 1

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ContractSetupScreen(
    state: ContractSetupState,
    navBack: () -> Unit,
    onLevelChanged: (level: Int) -> Unit,
    onXpChanged: (xp: Int) -> Unit,
    onFreeOnlyChanged: (freeOnly: Boolean) -> Unit,
    setContractProgress: () -> Unit,
) {
    val levels by remember(state.contract) {
        derivedStateOf {
            state.contract?.content?.chapters?.flatMap { it.levels }?.drop(PAGE_OFFSET)
        }
    }
    val pagerState = rememberPagerState(pageCount = { levels?.count() ?: 0 })
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    val targetPage by remember { derivedStateOf { pagerState.targetPage } }

    var clickedPage by remember { mutableIntStateOf(0) }
    var isHighest by remember { mutableStateOf(false) }

    var xpTotal by remember { mutableIntStateOf(0) }
    var xpCollectedString by remember { mutableStateOf(state.xp.toString()) }
    var xpCollectedError by remember { mutableStateOf<UiText?>(null) }

    var showFreeOnlyTooltip by remember { mutableStateOf(false) }

    LaunchedEffect(currentPage, targetPage) {
        if (targetPage == currentPage) {
            val newLevel = currentPage + PAGE_OFFSET
            if (newLevel != state.level) onLevelChanged(newLevel)
        }
    }

    LaunchedEffect(levels, state.level) {
        val newPage = state.level - PAGE_OFFSET
        pagerState.animateScrollToPage(newPage)

        isHighest = levels?.lastIndex == newPage
        xpTotal = levels?.getOrNull(newPage)?.xp ?: 0

        if (!isHighest && state.xp > xpTotal) {
            xpCollectedString = xpTotal.toString()
        }
    }

    LaunchedEffect(levels, xpCollectedString) {
        try {
            val uncheckedXpCollected = xpCollectedString.toInt()
            if (uncheckedXpCollected == state.xp) return@LaunchedEffect

            if (uncheckedXpCollected > xpTotal && !isHighest) {
                xpCollectedError =
                    UiText.StringResource(R.string.onboarding_contractSetup_xpCollected_error)
            } else {
                onXpChanged(uncheckedXpCollected)
                xpCollectedError = null
            }
        } catch (_: NumberFormatException) {
            xpCollectedError =
                UiText.StringResource(R.string.error_numberFormat)
        }
    }

    LaunchedEffect(levels, state.xp) {
        val newXpCollectedString = state.xp.toString()
        if (xpCollectedString != newXpCollectedString) {
            xpCollectedString = newXpCollectedString
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
                    val level = levels?.getOrNull(thisPage)
                    val reward = level?.rewards?.find { !it.isFreeReward }?.relation

                    val pageOffset = pagerState.getOffsetDistanceInPages(thisPage).absoluteValue
                    val topPadding = (32f * minOf(pageOffset, 1f)).dp
                    val bottomPadding = Spacing.xxl - topPadding
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
                        data = if (reward == null || state.contract == null) null else RewardCardData(
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
                        xpCollected = if (thisPage < state.level - PAGE_OFFSET) level?.xp
                            ?: 0 else if (thisPage == state.level - PAGE_OFFSET) state.xp else 0,
                        isOwned = thisPage < state.level - PAGE_OFFSET,
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

                LabeledSwitch(
                    modifier = Modifier.fillMaxWidth(),
                    label = UiText.StringResource(R.string.onboarding_contractSetup_freeOnly_label)
                        .asString(),
                    value = state.freeOnly,
                    onValueChange = onFreeOnlyChanged,
                    showTooltip = true,
                    onTooltip = { showFreeOnlyTooltip = true },
                )

                Spacer(modifier = Modifier.height(Spacing.xl))

                OnboardingButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onDismiss = navBack,
                    onContinue = setContractProgress,
                    disableContinueButton = xpCollectedError != null || levels == null,
                    dismissText = UiText.StringResource(R.string.button_back),
                    continueText = UiText.StringResource(R.string.button_continue)
                )
            }
        }
    )

    if (showFreeOnlyTooltip) {
        FreeOnlyInfoDialog(
            onDismiss = { showFreeOnlyTooltip = false },
        )
    }
}

@ScreenPreviewAnnotations
@Composable
fun ContractSetupScreenPreview() {
    ValolinkTheme {
        Surface {
            ContractSetupScreen(
                state = ContractSetupState(),
                navBack = {},
                onLevelChanged = {},
                onXpChanged = {},
                onFreeOnlyChanged = {},
                setContractProgress = {}
            )
        }
    }
}

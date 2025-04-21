/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractSetupScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   21.04.25, 17:24
 */

package dev.bittim.valolink.onboarding.ui.screens.contractSetup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.bittim.valolink.core.ui.components.rewardCard.RewardCard
import dev.bittim.valolink.core.ui.components.rewardCard.RewardCardData
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations
import dev.bittim.valolink.core.ui.util.extensions.modifier.SATURATION_DESATURATED
import dev.bittim.valolink.core.ui.util.extensions.modifier.saturation
import dev.bittim.valolink.onboarding.ui.components.OnboardingLayout
import kotlin.math.absoluteValue

@Composable
fun ContractSetupScreen(
    state: ContractSetupState
) {
    val levels = state.contract?.content?.chapters?.flatMap { it.levels }
    val pagerState = rememberPagerState(pageCount = { levels?.count() ?: 0 })
    var clickedPage by remember { mutableIntStateOf(0) }
    var page by remember { mutableIntStateOf(0) }
    var isHighest by remember { mutableStateOf(false) }

    var xpTotal by remember { mutableIntStateOf(0) }
    var xpCollected by remember { mutableIntStateOf(0) }
    var xpCollectedString by remember { mutableStateOf("") }

    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow { pagerState.currentPage }.collect {
            page = it
            xpTotal = levels?.get(page)?.xp ?: 0
            isHighest = it == levels?.lastIndex

            if (!isHighest && xpCollected > xpTotal) {
                xpCollected = xpTotal
            }
        }
    }

    LaunchedEffect(xpCollected) {
        xpCollectedString = xpCollected.toString()
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
                HorizontalPager(
                    state = pagerState
                ) {
                    val level = levels?.get(it)
                    val reward = level?.rewards?.find { !it.isFreeReward }?.relation

                    val pageOffset = pagerState.getOffsetDistanceInPages(it).absoluteValue
                    val verticalPadding = (32f * pageOffset).dp
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
                            .padding(Spacing.xs, verticalPadding)
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
                            xpTotal = xpTotal,
                            xpCollected = if (it < page) level.xp else if (it == page) xpCollected else 0,
                            price = level.vpCost,
                            amount = reward.amount,
                            currencyIcon = "",
                            isOwned = it < page
                        ),
                        onNavToLevelDetails = { },
                    )
                }
            }
        },
        form = { }
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

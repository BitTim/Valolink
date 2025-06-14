/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MatchesAddScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.06.25, 02:07
 */

package dev.bittim.valolink.content.ui.screens.content.matches.add

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.bittim.valolink.R
import dev.bittim.valolink.content.domain.model.map.MapType
import dev.bittim.valolink.content.ui.screens.content.matches.components.MapCard
import dev.bittim.valolink.content.ui.screens.content.matches.components.MapCardData
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.extensions.modifier.SATURATION_DESATURATED
import dev.bittim.valolink.core.ui.util.extensions.modifier.saturation
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MatchesAddScreen(
    state: MatchesAddState,
    onNavBack: () -> Unit,
) {
    val maps by remember(state.maps) {
        derivedStateOf {
            state.maps?.filter { it.type == MapType.Default || it.type == MapType.TDM }
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val pagerState = rememberPagerState(pageCount = { maps?.count() ?: 0 })
    var clickedPage by remember { mutableIntStateOf(0) }

    LaunchedEffect(clickedPage) {
        pagerState.animateScrollToPage(clickedPage)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
    ) {
        TopAppBar(
            title = { Text(text = UiText.StringResource(R.string.matches_add_title).asString()) },
            navigationIcon = {
                IconButton(onClick = { onNavBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            scrollBehavior = scrollBehavior,
            windowInsets = WindowInsets.statusBars
        )

        val contentPadding = (LocalConfiguration.current.screenWidthDp.dp - MapCard.width) / 2

        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(MapCard.width),
            contentPadding = PaddingValues(horizontal = contentPadding),
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(pagerState.pageCount)
            ),
        ) { thisPage ->
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

            val map = maps?.getOrNull(thisPage)

            MapCard(
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
                    }
                    .clickable(
                        onClick = {
                            clickedPage = thisPage
                        }
                    ),
                data = if (map == null) null else MapCardData(
                    mapImage = map.listViewIconTall,
                    mapName = map.displayName,
                    mapCoordinates = map.coordinates,
                )
            )
        }
    }
}
/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       MatchesAddScreen.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   15.06.25, 16:00
 */

package dev.bittim.valolink.content.ui.screens.content.matches.add

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.OutlinedFlag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButtonShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.domain.model.map.MapType
import dev.bittim.valolink.content.domain.model.mode.ScoreType
import dev.bittim.valolink.content.ui.screens.content.matches.components.MapCard
import dev.bittim.valolink.content.ui.screens.content.matches.components.MapCardData
import dev.bittim.valolink.content.ui.screens.content.matches.components.ScoreChip
import dev.bittim.valolink.core.domain.model.ScoreResult
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.annotations.ScreenPreviewAnnotations
import dev.bittim.valolink.core.ui.util.extensions.modifier.SATURATION_DESATURATED
import dev.bittim.valolink.core.ui.util.extensions.modifier.pulseAnimation
import dev.bittim.valolink.core.ui.util.extensions.modifier.saturation
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MatchesAddScreen(
    state: MatchesAddState,
    determineScoreResult: (score: Int, enemyScore: Int?, surrender: Boolean, enemySurrender: Boolean) -> ScoreResult,
    onNavBack: () -> Unit,
) {
    val maps by remember(state.maps) {
        derivedStateOf {
            state.maps?.filter { it.type == MapType.Default || it.type == MapType.TDM }
        }
    }

    val modes by remember(state.modes) {
        derivedStateOf {
            state.modes?.filter { it.scoreType != ScoreType.None }
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val pagerState = rememberPagerState(pageCount = { maps?.count() ?: 0 })
    var clickedPage by remember { mutableIntStateOf(0) }

    var mode by remember { mutableStateOf(modes?.firstOrNull()) }
    var isModeMenuExpanded by remember { mutableStateOf(false) }
    var isRanked by remember { mutableStateOf(false) }

    var scoreString by remember { mutableStateOf("") }
    var enemyScoreString by remember { mutableStateOf("") }
    var surrender by remember { mutableStateOf(false) }
    var enemySurrender by remember { mutableStateOf(false) }

    val score by remember { derivedStateOf { scoreString.toIntOrNull() ?: 0 } }
    val enemyScore by remember { derivedStateOf { enemyScoreString.toIntOrNull() ?: 0 } }

    LaunchedEffect(clickedPage) {
        pagerState.animateScrollToPage(clickedPage)
    }

    LaunchedEffect(modes) {
        if (mode == null) mode = modes?.firstOrNull()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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

        Spacer(modifier = Modifier.height(Spacing.xl))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.l),
            verticalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    Text(
                        text = UiText.StringResource(R.string.matches_add_summary_title).asString(),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = UiText.StringResource(R.string.matches_add_summary_description)
                            .asString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                ScoreChip(
                    score = score,
                    enemyScore = enemyScore,
                    surrender = surrender || enemySurrender,
                    scoreResult = determineScoreResult(
                        score,
                        enemyScore,
                        surrender,
                        enemySurrender
                    ),
                    compact = false
                )
            }

            Crossfade(modes) {
                if (it == null || mode == null) {
                    Box(
                        modifier = Modifier
                            .height(OutlinedTextFieldDefaults.MinHeight)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(Spacing.m))
                            .pulseAnimation(),
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.l),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val rotation = if (isModeMenuExpanded) 180f else 0f
                        val animatedRotation = animateFloatAsState(targetValue = rotation)

                        Box(
                            modifier = Modifier.weight(1f),
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = mode!!.displayName,
                                onValueChange = {},
                                readOnly = true,
                                label = {
                                    Text(
                                        text = UiText.StringResource(R.string.matches_add_mode_label)
                                            .asString()
                                    )
                                },
                                leadingIcon = {
                                    AsyncImage(
                                        modifier = Modifier
                                            .size(Spacing.xl)
                                            .aspectRatio(1f),
                                        model = mode!!.displayIcon,
                                        contentDescription = mode!!.displayName,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                                        contentScale = ContentScale.Fit,
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        isModeMenuExpanded = !isModeMenuExpanded
                                    }) {
                                        Icon(
                                            modifier = Modifier.rotate(animatedRotation.value),
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }

                        AnimatedVisibility(mode!!.canBeRanked) {
                            FilledTonalIconToggleButton(
                                modifier = Modifier.size(
                                    IconButtonDefaults.mediumContainerSize(
                                        IconButtonDefaults.IconButtonWidthOption.Wide
                                    )
                                ),
                                checked = isRanked,
                                onCheckedChange = { isRanked = it },
                                shapes = IconToggleButtonShapes(
                                    shape = IconButtonDefaults.mediumRoundShape,
                                    pressedShape = IconButtonDefaults.mediumPressedShape,
                                    checkedShape = IconButtonDefaults.mediumSelectedRoundShape
                                )
                            ) {
                                Icon(
                                    imageVector = if (isRanked) Icons.Filled.MilitaryTech else Icons.Outlined.MilitaryTech,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }

            Crossfade(modes) {
                if (it == null || mode == null) {
                    Box(
                        modifier = Modifier
                            .height(OutlinedTextFieldDefaults.MinHeight)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(Spacing.m))
                            .pulseAnimation(),
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.m),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        OutlinedIconToggleButton(
                            checked = surrender,
                            onCheckedChange = {
                                surrender = it
                                enemySurrender = false
                            },
                            modifier = Modifier.size(
                                IconButtonDefaults.mediumContainerSize(
                                    IconButtonDefaults.IconButtonWidthOption.Narrow
                                )
                            ),
                            shapes = IconToggleButtonShapes(
                                shape = IconButtonDefaults.mediumRoundShape,
                                pressedShape = IconButtonDefaults.mediumPressedShape,
                                checkedShape = IconButtonDefaults.mediumSelectedRoundShape
                            )
                        ) {
                            Icon(
                                imageVector = if (surrender) Icons.Filled.Flag else Icons.Outlined.OutlinedFlag,
                                contentDescription = null
                            )
                        }

                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = scoreString,
                            onValueChange = { scoreString = it },
                            label = {
                                Text(
                                    text = UiText.StringResource(R.string.matches_add_score_label)
                                        .asString()
                                )
                            }
                        )

                        OutlinedIconToggleButton(
                            checked = enemySurrender,
                            onCheckedChange = {
                                enemySurrender = it
                                surrender = false
                            },
                            modifier = Modifier.size(
                                IconButtonDefaults.mediumContainerSize(
                                    IconButtonDefaults.IconButtonWidthOption.Narrow
                                )
                            ),
                            shapes = IconToggleButtonShapes(
                                shape = IconButtonDefaults.mediumRoundShape,
                                pressedShape = IconButtonDefaults.mediumPressedShape,
                                checkedShape = IconButtonDefaults.mediumSelectedRoundShape
                            )
                        ) {
                            Icon(
                                imageVector = if (enemySurrender) Icons.Filled.Flag else Icons.Outlined.OutlinedFlag,
                                contentDescription = null
                            )
                        }

                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = enemyScoreString,
                            onValueChange = { enemyScoreString = it },
                            label = {
                                Text(
                                    text = UiText.StringResource(R.string.matches_add_enemyScore_label)
                                        .asString()
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@ScreenPreviewAnnotations
@Composable
fun MatchesAddScreenPreview() {
    ValolinkTheme {
        Surface {
            MatchesAddScreen(
                state = MatchesAddState(),
                determineScoreResult = { _, _, _, _ -> ScoreResult.Draw },
                onNavBack = {},
            )
        }
    }
}
/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RelationsSection.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.leveldetails.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.automirrored.outlined.Redo
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.R
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import dev.bittim.valolink.core.ui.components.ConnectedButtonEntry
import dev.bittim.valolink.core.ui.components.SingleConnectedButtonGroup
import dev.bittim.valolink.core.ui.components.rewardCard.RewardListCard
import dev.bittim.valolink.core.ui.components.rewardCard.RewardListCardData
import dev.bittim.valolink.core.ui.theme.Spacing
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.core.ui.util.ToggleIcon
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.core.ui.util.extensions.modifier.pulseAnimation
import dev.bittim.valolink.core.ui.util.getScaledLineHeightFromFontStyle
import javax.annotation.concurrent.Immutable

@Immutable
data class RelationsSectionRelation(
    val level: RewardListCardData?,
    val icon: ToggleIcon,
)

@Immutable
data class RelationsSectionContentData(
    val relations: List<RelationsSectionRelation>,
)

@Immutable
data class RelationsSectionState(
    val xpCollected: Int,
    val isLocked: Boolean,
    val isOwned: Boolean,
)

@Immutable
data class RelationsSectionUserData(
    val relations: List<RelationsSectionState>,
)

@Composable
fun RelationsSection(
    modifier: Modifier = Modifier,
    contentData: RelationsSectionContentData?,
    userData: RelationsSectionUserData?,
    onNavToLevelDetails: (String) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    var selectedRelation by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState {
        contentData?.relations?.count() ?: 1
    }

    LaunchedEffect(selectedRelation) {
        pagerState.animateScrollToPage(selectedRelation)
    }

    LaunchedEffect(
        pagerState.currentPage
    ) {
        selectedRelation = pagerState.currentPage
    }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Spacing.l, end = Spacing.l),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = Spacing.s),
            ) {
                Text(
                    text = UiText.StringResource(R.string.levelDetails_relations_title).asString(),
                    style = MaterialTheme.typography.titleLarge,
                )

                Crossfade(
                    modifier = Modifier.animateContentSize(),
                    targetState = contentData,
                    label = "Relation name Loading"
                ) {
                    if (it == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(
                                    getScaledLineHeightFromFontStyle(
                                        density,
                                        configuration,
                                        MaterialTheme.typography.labelMedium
                                    )
                                )
                                .clip(MaterialTheme.shapes.medium)
                                .pulseAnimation()
                        )
                    } else {
                        Text(
                            text = it.relations.getOrNull(selectedRelation)?.icon?.contentDescription?.asString()
                                ?: "",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            AnimatedContent(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.End),
                targetState = contentData,
                label = "Relation buttons Loading",
            ) { checkedData ->
                if (checkedData == null) {
                    Box(
                        modifier = Modifier
                            .width(ButtonDefaults.MinWidth * 2)
                            .height(ButtonDefaults.MinHeight)
                            .clip(ButtonDefaults.filledTonalShape)
                            .pulseAnimation()
                    )
                } else {
                    SingleConnectedButtonGroup(
                        modifier = Modifier.fillMaxWidth(),
                        entries = checkedData.relations.map {
                            ConnectedButtonEntry(
                                label = null,
                                icon = it.icon
                            )
                        },
                        onSelectedChanged = { selectedRelation = it }
                    )
                }
            }
        }

        Spacer(Modifier.height(Spacing.s))

        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            state = pagerState
        ) { index ->
            RewardListCard(
                modifier = Modifier.padding(start = Spacing.l, end = Spacing.l),
                data = contentData?.relations?.get(index)?.level,
                xpCollected = userData?.relations?.get(index)?.xpCollected ?: 0,
                isLocked = userData?.relations?.get(index)?.isLocked == true,
                isOwned = userData?.relations?.get(index)?.isOwned == true,
                onNavToLevelDetails = onNavToLevelDetails
            )
        }

        Spacer(Modifier.height(Spacing.xl))
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RelationsSectionPreview() {
    ValolinkTheme {
        Surface {
            RelationsSection(
                contentData = RelationsSectionContentData(
                    relations = listOf(
                        RelationsSectionRelation(
                            level = RewardListCardData(
                                name = "Previous Card",
                                levelUuid = "",
                                type = RewardType.PLAYER_CARD,
                                levelName = "Level 7",
                                contractName = "Clove Contract",
                                rewardCount = 1,
                                amount = 1,
                                useXP = true,
                                xpTotal = 10000,
                                displayIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                                background = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/background.png"
                            ),
                            icon = ToggleIcon(
                                Icons.AutoMirrored.Filled.Undo,
                                Icons.AutoMirrored.Outlined.Undo,
                                UiText.StringResource(R.string.relation_previous)
                            )
                        ),
                        RelationsSectionRelation(
                            level = RewardListCardData(
                                name = "Next Card",
                                levelUuid = "",
                                type = RewardType.PLAYER_CARD,
                                levelName = "Level 9",
                                contractName = "Clove Contract",
                                rewardCount = 1,
                                amount = 1,
                                useXP = true,
                                xpTotal = 10000,
                                displayIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                                background = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/background.png"
                            ),
                            icon = ToggleIcon(
                                Icons.AutoMirrored.Filled.Redo,
                                Icons.AutoMirrored.Outlined.Redo,
                                UiText.StringResource(R.string.relation_next)
                            )
                        ),
                    )
                ),
                userData = RelationsSectionUserData(
                    relations = listOf(
                        RelationsSectionState(
                            xpCollected = 0,
                            isLocked = false,
                            isOwned = true
                        ),
                        RelationsSectionState(
                            xpCollected = 0,
                            isLocked = false,
                            isOwned = true
                        ),
                    )
                ),
                onNavToLevelDetails = {}
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoadingRelationsSectionPreview() {
    ValolinkTheme {
        Surface {
            RelationsSection(
                contentData = null,
                userData = null,
                onNavToLevelDetails = {}
            )
        }
    }
}

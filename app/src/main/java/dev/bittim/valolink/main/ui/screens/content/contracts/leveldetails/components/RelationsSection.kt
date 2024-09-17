package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardType
import dev.bittim.valolink.main.ui.components.pulseAnimation
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.components.AgentRewardListCard
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.components.AgentRewardListCardData
import dev.bittim.valolink.main.ui.util.getScaledLineHeightFromFontStyle
import javax.annotation.concurrent.Immutable

@Immutable
data class RelationsSectionRelation(
    val level: AgentRewardListCardData?,
    val name: String,
    val icon: ImageVector,
)

@Immutable
data class RelationsSectionData(
    val relations: List<RelationsSectionRelation>,
)

@Composable
fun RelationsSection(
    modifier: Modifier = Modifier,
    data: RelationsSectionData?,
    onNavToLevelDetails: (String) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    var selectedRelation by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState {
        data?.relations?.count() ?: 1
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
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(end = 8.dp),
            ) {
                Text(
                    text = "Relations",
                    style = MaterialTheme.typography.titleLarge,
                )

                Crossfade(
                    modifier = Modifier.animateContentSize(),
                    targetState = data,
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
                            text = it.relations.getOrNull(selectedRelation)?.name ?: "",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            AnimatedContent(
                modifier = Modifier.wrapContentWidth(Alignment.End),
                targetState = data,
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
                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        checkedData.relations.forEachIndexed { index, relation ->
                            val isSelected = selectedRelation == index

                            FilledTonalIconToggleButton(
                                checked = isSelected,
                                onCheckedChange = {
                                    if (it) {
                                        selectedRelation = index
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = relation.icon,
                                    contentDescription = relation.name
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            state = pagerState
        ) { index ->
            AgentRewardListCard(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                data = data?.relations?.get(index)?.level,
                showMenuButton = false,
                onNavToLevelDetails = onNavToLevelDetails
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RelationsSectionPreview() {
    ValolinkTheme {
        Surface {
            RelationsSection(
                data = RelationsSectionData(
                    relations = listOf(
                        RelationsSectionRelation(
                            level = AgentRewardListCardData(
                                name = "Previous Card",
                                levelUuid = "",
                                type = RewardType.PLAYER_CARD,
                                levelName = "Level 7",
                                contractName = "Clove Contract",
                                rewardCount = 1,
                                amount = 1,
                                displayIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                                background = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/background.png"
                            ),
                            name = "Previous",
                            icon = Icons.AutoMirrored.Default.Undo
                        ),
                        RelationsSectionRelation(
                            level = AgentRewardListCardData(
                                name = "Next Card",
                                levelUuid = "",
                                type = RewardType.PLAYER_CARD,
                                levelName = "Level 9",
                                contractName = "Clove Contract",
                                rewardCount = 1,
                                amount = 1,
                                displayIcon = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/displayicon.png",
                                background = "https://media.valorant-api.com/playercards/d6dbc61e-49f4-c28e-baa2-79b23cdb6499/background.png"
                            ),
                            name = "Next",
                            icon = Icons.AutoMirrored.Default.Redo
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
                data = null,
                onNavToLevelDetails = {}
            )
        }
    }
}
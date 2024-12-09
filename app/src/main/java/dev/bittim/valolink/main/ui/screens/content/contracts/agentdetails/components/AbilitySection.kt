package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.components

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.content.domain.model.agent.Ability
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.components.pulseAnimation

@Composable
fun AbilitySection(
    modifier: Modifier = Modifier,
    abilities: List<Ability>?,
) {
    var selectedAbility by remember { mutableIntStateOf(0) }
    val abilityPagerState = rememberPagerState {
        abilities?.count() ?: 0
    }

    LaunchedEffect(selectedAbility) {
        abilityPagerState.animateScrollToPage(selectedAbility)
    }

    LaunchedEffect(
        abilityPagerState.currentPage
    ) {
        selectedAbility = abilityPagerState.currentPage
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Crossfade(
            modifier = Modifier.animateContentSize(),
            targetState = abilities,
            label = "Ability tabs loading"
        ) {
            if (it == null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .height(ButtonDefaults.MinHeight)
                                .padding(1.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .pulseAnimation()
                        )
                    }
                }
            } else {
                TabRow(selectedTabIndex = selectedAbility) {
                    it.forEachIndexed { index, ability ->
                        val isSelected = index == selectedAbility

                        Tab(selected = isSelected,
                            onClick = { selectedAbility = index },
                            icon = {
                                AsyncImage(
                                    modifier = Modifier.padding(12.dp),
                                    model = ability.displayIcon,
                                    contentDescription = ability.displayName,
                                    colorFilter = ColorFilter.tint(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant),
                                    placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_ability_icon)
                                )
                            }
                        )
                    }
                }
            }
        }


        Crossfade(
            modifier = Modifier.animateContentSize(),
            targetState = abilities,
            label = "Ability details loading"
        ) {
            if (it == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(17.dp)
                        .clip(MaterialTheme.shapes.large)
                        .pulseAnimation()
                )
            } else {
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    verticalAlignment = Alignment.Top,
                    state = abilityPagerState
                ) { index ->
                    val ability = abilities?.get(index)

                    Card(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        AbilityDetailsItem(
                            modifier = Modifier.padding(16.dp),
                            displayName = ability?.displayName ?: "",
                            slot = ability?.slot ?: "",
                            description = ability?.description ?: "",
                            displayIcon = ability?.displayIcon
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AbilitySectionPreview() {
    ValolinkTheme {
        Surface {
            AbilitySection(
                abilities = listOf(
                    Ability(
                        agent = "",
                        displayName = "Ability 1",
                        slot = "Slot 1",
                        description = "Description 1",
                        displayIcon = ""
                    ),
                    Ability(
                        agent = "",
                        displayName = "Ability 2",
                        slot = "Slot 2",
                        description = "Description 2",
                        displayIcon = ""
                    ),
                    Ability(
                        agent = "",
                        displayName = "Ability 3",
                        slot = "Slot 3",
                        description = "Description 3",
                        displayIcon = ""
                    ),
                    Ability(
                        agent = "",
                        displayName = "Ability 4",
                        slot = "Slot 4",
                        description = "Description 4",
                        displayIcon = ""
                    )
                )
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoadingAbilitySectionPreview() {
    ValolinkTheme {
        Surface {
            AbilitySection(abilities = null)
        }
    }
}
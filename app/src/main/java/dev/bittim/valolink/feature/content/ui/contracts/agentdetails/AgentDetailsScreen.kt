package dev.bittim.valolink.feature.content.ui.contracts.agentdetails

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.feature.content.domain.model.agent.Agent
import dev.bittim.valolink.feature.content.domain.model.agent.Role
import dev.bittim.valolink.feature.content.domain.model.contract.Chapter
import dev.bittim.valolink.feature.content.domain.model.contract.ChapterLevel
import dev.bittim.valolink.feature.content.domain.model.contract.Content
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import dev.bittim.valolink.feature.content.domain.model.contract.Reward
import dev.bittim.valolink.feature.content.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.feature.content.ui.contracts.agentdetails.components.AgentRewardCard
import dev.bittim.valolink.feature.content.ui.contracts.components.AgentBackdrop
import dev.bittim.valolink.ui.theme.ValolinkTheme
import java.util.UUID
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentDetailsScreen(
    state: AgentDetailsState,
    onNavBack: () -> Unit,
    onNavGearRewardsList: () -> Unit
) {
    if (state.isLoading) CircularProgressIndicator() // TODO: Temporary

    if (state.agentGear != null && state.agentGear.content.relation is Agent) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                colors = CardDefaults.cardColors().copy(contentColor = Color.White),
                shape = MaterialTheme.shapes.large.copy(
                    topStart = CornerSize(0.dp),
                    topEnd = CornerSize(0.dp)
                )
            ) {
                AgentBackdrop(
                    modifier = Modifier.fillMaxSize(),
                    useGradient = true,
                    backgroundGradientColors = state.agentGear.content.relation.backgroundGradientColors,
                    backgroundImage = state.agentGear.content.relation.background,
                    isDisabled = false
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = state.agentGear.content.relation.fullPortrait,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_full_portrait)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = state.agentGear.content.relation.displayName,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .height(16.dp)
                                        .aspectRatio(1f),
                                    model = state.agentGear.content.relation.role.displayIcon,
                                    contentDescription = null,
                                    placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_role_icon)
                                )
                                Text(
                                    text = state.agentGear.content.relation.role.displayName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                        val unlockedLevels = 3
                        val totalLevels = state.agentGear.calcLevelCount()
                        val percentage =
                            floor((unlockedLevels.toFloat() / totalLevels.toFloat()) * 100f)

                        val animatedProgress: Float by animateFloatAsState(
                            targetValue = (percentage / 100f),
                            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                            label = ""
                        )

                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            trackColor = Color.Black.copy(alpha = 0.3f),
                            progress = { animatedProgress })

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "$unlockedLevels / $totalLevels")
                            Text(text = "${percentage.toInt()} %")
                        }
                    }

                    TopAppBar(
                        title = { Text(text = state.agentGear.content.relation.displayName) },
                        navigationIcon = {
                            IconButton(onClick = onNavBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors().copy(
                            containerColor = Color.Transparent,
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White,
                            actionIconContentColor = Color.White.copy(alpha = 0.7f)
                        )
                    )
                }
            }

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rewards",
                            style = MaterialTheme.typography.titleLarge
                        )

                        IconButton(onClick = onNavGearRewardsList) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                                contentDescription = null
                            )
                        }
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.agentGear.content.chapters.flatMap { chapter -> chapter.levels },
                            itemContent = {
                                AgentRewardCard(
                                    name = it.reward.uuid,
                                    type = it.reward.type,
                                    price = it.doughCost,
                                    displayIcon = "",
                                    currencyIcon = ""
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Details",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = state.agentGear.content.relation.displayName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = state.agentGear.content.relation.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .height(16.dp)
                                    .aspectRatio(1f),
                                model = state.agentGear.content.relation.role.displayIcon,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_role_icon)
                            )
                            Text(
                                text = state.agentGear.content.relation.role.displayName,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Text(
                            text = state.agentGear.content.relation.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgentDetailsScreenPreview() {
    ValolinkTheme {
        AgentDetailsScreen(
            state = AgentDetailsState(
                isLoading = false,
                agentGear = Contract(
                    UUID.randomUUID().toString(),
                    "Clove Gear",
                    false,
                    -1,
                    Content(
                        Agent(
                            UUID.randomUUID().toString(),
                            "Clove",
                            "Scottish troublemaker Clove makes mischief for enemies in both the heat of combat and the cold of death. The young immortal keeps foes guessing, even from beyond the grave, their return to the living only ever a moment away.",
                            "Smonk",
                            listOf(),
                            "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/displayicon.png",
                            "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/displayiconsmall.png",
                            "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/fullportrait.png",
                            "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/fullportrait.png",
                            "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/fullportrait.png",
                            "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/killfeedportrait.png",
                            "https://media.valorant-api.com/agents/1dbf2edd-4729-0984-3115-daa5eed44993/background.png",
                            listOf(
                                "f17cadff",
                                "062261ff",
                                "c347c7ff",
                                "f1db6fff"
                            ),
                            false,
                            true,
                            false,
                            Role(
                                UUID.randomUUID().toString(),
                                "Controller",
                                "Controllers are experts in slicing up dangerous territory to set their team up for success.",
                                "https://media.valorant-api.com/agents/roles/4ee40330-ecdd-4f2f-98a8-eb1243428373/displayicon.png"
                            ),
                            listOf()
                        ),
                        -1,
                        listOf(
                            Chapter(
                                listOf(
                                    ChapterLevel(
                                        20000,
                                        0,
                                        false,
                                        2000,
                                        true,
                                        Reward(
                                            "Spray",
                                            "7221ab04-4a64-9a0f-ba1e-e7a423f5ed4b",
                                            1,
                                            false
                                        )
                                    ),
                                    ChapterLevel(
                                        30000,
                                        0,
                                        false,
                                        2500,
                                        true,
                                        Reward(
                                            "PlayerCard",
                                            "faa3c3b5-4b0b-1f20-b383-01b7b83126ff",
                                            1,
                                            false
                                        )
                                    )
                                ),
                                null,
                                false
                            )
                        )
                    ),
                    null,
                    null
                )
            ),
            onNavBack = {},
            onNavGearRewardsList = {}
        )
    }
}
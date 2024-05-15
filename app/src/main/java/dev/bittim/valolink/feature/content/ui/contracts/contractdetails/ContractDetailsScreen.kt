package dev.bittim.valolink.feature.content.ui.contracts.contractdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.feature.content.domain.model.Season
import dev.bittim.valolink.feature.content.domain.model.contract.Chapter
import dev.bittim.valolink.feature.content.domain.model.contract.ChapterLevel
import dev.bittim.valolink.feature.content.domain.model.contract.Content
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import dev.bittim.valolink.feature.content.domain.model.contract.Reward
import dev.bittim.valolink.feature.content.ui.contracts.agentdetails.components.AgentRewardCard
import dev.bittim.valolink.ui.theme.ValolinkTheme
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractDetailsScreen(
    state: ContractDetailsState, onNavBack: () -> Unit, onNavContractRewardsList: () -> Unit
) {
    if (state.isLoading) CircularProgressIndicator() // TODO: Temporary

    if (state.contract != null) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val scrollState = rememberScrollState()

        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                TopAppBar(title = {
                    Text(text = state.contract.displayName)
                }, navigationIcon = {
                    IconButton(onClick = onNavBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }, actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert, contentDescription = null
                        )
                    }
                }, scrollBehavior = scrollBehavior
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rewards", style = MaterialTheme.typography.titleLarge
                        )

                        IconButton(onClick = onNavContractRewardsList) {
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
                        items(items = state.rewards, itemContent = {
                            AgentRewardCard(
                                name = it.first.displayName,
                                type = it.first.type,
                                displayIcon = it.first.displayIcon,
                                price = it.second.vpCost, amount = it.first.amount,
                                currencyIcon = state.vp?.displayIcon ?: ""
                            )
                        })
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Free Rewards", style = MaterialTheme.typography.titleLarge
                        )

                        IconButton(onClick = onNavContractRewardsList) {
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
                        items(items = state.freeRewards, itemContent = {
                            AgentRewardCard(
                                name = it.first.displayName,
                                type = it.first.type,
                                displayIcon = it.first.displayIcon,
                                price = it.second.vpCost, amount = it.first.amount,
                                currencyIcon = state.vp?.displayIcon ?: ""
                            )
                        })
                    }
                }
            }
        }
    }
}



@Preview
@Composable
fun ContractDetailsScreenPreview() {
    ValolinkTheme {
        ContractDetailsScreen(state = ContractDetailsState(
            isLoading = false, contract = Contract(
                UUID.randomUUID().toString(), "Imagination: Act III", false, -1, Content(
                    Season(
                        UUID.randomUUID().toString(),
                        "ACT III",
                        Instant.now().minus(10, ChronoUnit.DAYS),
                        Instant.now().plus(10, ChronoUnit.DAYS)
                    ), -1, listOf(
                        Chapter(
                            listOf(
                                ChapterLevel(
                                    2500, 200, true, 0, false, Reward(
                                        "Spray", UUID.randomUUID().toString(), 1, false
                                    )
                                )
                            ), listOf(), false
                        )
                    )
                )
            )
        ), onNavBack = {}, onNavContractRewardsList = {})
    }
}
package dev.bittim.valolink.main.ui.screens.content.contracts.agentlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.main.domain.model.game.agent.Agent
import dev.bittim.valolink.main.ui.screens.content.contracts.components.AgentCarouselCard
import dev.bittim.valolink.main.ui.screens.content.contracts.components.AgentCarouselCardData
import dev.bittim.valolink.main.ui.util.getProgressPercent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentListScreen(
    state: AgentListState,
    initUserContract: (String) -> Unit,
    onNavBack: () -> Unit,
    onNavToAgentDetails: (String) -> Unit,
) {
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = "Agents") },
            navigationIcon = {
                IconButton(onClick = { onNavBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            scrollBehavior = scrollBehaviour,
            windowInsets = WindowInsets.statusBars
        )

        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .nestedScroll(scrollBehaviour.nestedScrollConnection),
            columns = GridCells.Adaptive(AgentCarouselCard.minWidth),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(items = state.agentGears,
                  itemContent = { gear ->
                      val userContract =
                          state.userData?.contracts?.find { it.contract == gear.uuid }
                      val levelCount = gear.calcLevelCount()

                      if (userContract == null) {
                          initUserContract(gear.uuid)
                          return@items
                      }

                      if (gear.content.relation is Agent) {
                          AgentCarouselCard(
                              data = AgentCarouselCardData(
                                  backgroundGradientColors = gear.content.relation.backgroundGradientColors,
                                  backgroundImage = gear.content.relation.background,
                                  fullPortrait = gear.content.relation.fullPortrait,
                                  roleIcon = gear.content.relation.role.displayIcon,
                                  agentName = gear.content.relation.displayName,
                                  contractUuid = gear.uuid,
                                  roleName = gear.content.relation.role.displayName,
                                  totalLevels = levelCount,
                                  isLocked = !(state.userData.agents.any { it.agent == gear.content.relation.uuid }),
                                  unlockedLevels = userContract.levels.count(),
                                  percentage = getProgressPercent(
                                      userContract.levels.count(),
                                      levelCount
                                  )
                              ),
                              onNavToAgentDetails = onNavToAgentDetails
                          )
                      }
                  })
        }
    }
}
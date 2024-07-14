package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.main.ui.components.BaseDetailsScreen
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.screens.content.contracts.components.LevelBackdrop
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components.LevelHeader

@Composable
fun LevelDetailsScreen(
    state: LevelDetailsState,
    onNavBack: () -> Unit,
) {
    if (state.isLoading) CircularProgressIndicator() // TODO: Temporary

    if (state.level != null && state.level.reward.relation != null) {
        // --------------------------------
        //  Logic
        // --------------------------------

        val reward = state.level.reward.relation

        // --------------------------------
        //  Layout
        // --------------------------------

        BaseDetailsScreen(
            cardBackground = {
                LevelBackdrop(
                    isDisabled = false,
                    backgroundImage = reward.background
                ) {}
            },
            cardImage = {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = WindowInsets.displayCutout
                                .asPaddingValues()
                                .calculateTopPadding(),
                            end = 16.dp,
                            bottom = 16.dp,
                            start = 16.dp,
                        ),
                    model = reward.previewImage,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_full_portrait)
                )
            },
            cardContent = {},
            content = {
                LevelHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    displayName = reward.displayName,
                    type = reward.type,
                    displayIcon = reward.displayIcon,
                    levelName = state.level.name,
                    contractName = state.level.contractName
                )
            },
            dropdown = {},

            onOpenDropdown = {},
            onNavBack = onNavBack,
        )
    }
}
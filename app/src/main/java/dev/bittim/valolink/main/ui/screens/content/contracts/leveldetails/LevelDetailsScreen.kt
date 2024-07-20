package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.bittim.valolink.R
import dev.bittim.valolink.main.ui.components.BaseDetailsScreen
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder
import dev.bittim.valolink.main.ui.screens.content.contracts.components.LevelBackdrop
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components.LevelHeader
import dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components.VariantPreviewCluster

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
        var selectedVariant by remember {
            mutableIntStateOf(0)
        }

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
                Box {
                    Crossfade(
                        targetState = selectedVariant,
                        label = "Preview Image Transition"
                    ) {
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
                            model = ImageRequest
                                .Builder(
                                    LocalContext.current
                                )
                                .data(reward.previewImages[it].first ?: "")
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_full_portrait)
                        )
                    }
                }
            },
            cardContent = {
                VariantPreviewCluster(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    variants = reward.previewImages.map { it.second },
                    onSelected = { selectedVariant = it }
                )
            },
            content = {
                LevelHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    displayName = if (reward.amount > 1) "${reward.amount} ${reward.displayName}" else reward.displayName,
                    type = reward.type,
                    displayIcon = reward.displayIcon,
                    levelName = state.level.name,
                    contractName = state.level.contractName,
                    currencyIcon = state.unlockCurrency?.displayIcon ?: "",
                    price = state.price,
                    xpTotal = if (state.isGear) -1 else state.level.xp,
                    xpProgress = 25, // TODO: Replace with actual user values
                )
            },
            dropdown = {},

            onOpenDropdown = {},
            onNavBack = onNavBack,
        )
    }
}
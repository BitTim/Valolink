package dev.bittim.valolink.main.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.zIndex
import dev.bittim.valolink.core.ui.theme.ValolinkTheme

data object BaseDetailsScreen {
    const val MAX_CARD_HEIGHT_FRACTION: Float = 0.6f
    val minCardHeight: Dp = 204.dp
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseDetailsScreen(
    cardBackground: @Composable () -> Unit,
    cardImage: @Composable () -> Unit,
    cardContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
    dropdown: @Composable () -> Unit,

    onOpenDropdown: () -> Unit,
    onNavBack: () -> Unit,
) {
    // --------------------------------
    //  Logic
    // --------------------------------

    // Card size and scrolling
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val maxCardHeight =
        (configuration.screenHeightDp * BaseDetailsScreen.MAX_CARD_HEIGHT_FRACTION).dp

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollState = rememberScrollState()
    val cardSize = with(density) {
        max(
            BaseDetailsScreen.minCardHeight,
            maxCardHeight - scrollState.value.toDp()
        )
    }

    // --------------------------------
    //  Layout
    // --------------------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f),
        verticalArrangement = Arrangement.Top
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardSize),
            colors = CardDefaults.cardColors().copy(contentColor = Color.White),
            shape = MaterialTheme.shapes.extraLarge.copy(
                topStart = CornerSize(0.dp),
                topEnd = CornerSize(0.dp)
            ),
        ) {
            Box {
                cardBackground()
                cardImage()
                cardContent()

                TopAppBar(
                    title = { },
                    navigationIcon = {
                        FilledTonalIconButton(onClick = onNavBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        FilledTonalIconButton(onClick = onOpenDropdown) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }

                        dropdown()
                    },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                    )
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(top = cardSize - 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .offset(y = maxCardHeight - cardSize)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        content()
        Spacer(modifier = Modifier.height(maxCardHeight - cardSize + 16.dp))
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BaseDetailsScreenPreview() {
    ValolinkTheme {
        Surface {
            BaseDetailsScreen(
                cardBackground = {},
                cardImage = {},
                cardContent = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End
                    ) {
                        Button(
                            onClick = { }
                        ) {
                            Text("Sample Button")
                        }
                    }
                },
                content = {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Base Details Screen",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(text = "This is a sample details screen")
                    }
                },
                dropdown = {},
                onOpenDropdown = {},
                onNavBack = {}
            )
        }
    }
}
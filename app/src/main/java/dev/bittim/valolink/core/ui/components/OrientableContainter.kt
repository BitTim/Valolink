package dev.bittim.valolink.core.ui.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun OrientableContainer(
    modifier: Modifier = Modifier,
    portraitContainer: @Composable (modifier: Modifier, content: @Composable () -> Unit) -> Unit,
    landscapeContainer: @Composable (modifier: Modifier, content: @Composable () -> Unit) -> Unit,
    content: @Composable (orientation: Int) -> Unit
) {
    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current

    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }.collect { orientation = it }
    }

    when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            landscapeContainer(modifier) {
                content(orientation)
            }
        }

        else -> {
            portraitContainer(modifier) {
                content(orientation)
            }
        }
    }
}
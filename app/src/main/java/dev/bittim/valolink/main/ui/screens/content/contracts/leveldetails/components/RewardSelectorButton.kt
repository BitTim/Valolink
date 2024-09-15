package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.main.ui.components.coilDebugPlaceholder

@Composable
fun RewardSelectorButton(
    modifier: Modifier = Modifier,
    displayIcon: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    FilledIconToggleButton(
        modifier = modifier
            .padding(4.dp)
            .height(52.dp)
            .aspectRatio(1f),
        shape = MaterialTheme.shapes.large,
        checked = checked,
        onCheckedChange = onCheckedChange
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .clip(MaterialTheme.shapes.medium),
            model = displayIcon,
            contentDescription = null,
            placeholder = coilDebugPlaceholder(debugPreview = R.drawable.debug_agent_reward_card_displayicon)
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RewardSelectorButtonPreview() {
    ValolinkTheme {
        Surface {
            RewardSelectorButton(
                displayIcon = "https://media.valorant-api.com/weaponskinchromas/2984c2a8-40e1-69eb-36b2-bea3c6f9b82d/swatch.png",
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun CheckedRewardSelectorButtonPreview() {
    ValolinkTheme {
        Surface {
            RewardSelectorButton(
                displayIcon = "https://media.valorant-api.com/weaponskinchromas/2984c2a8-40e1-69eb-36b2-bea3c6f9b82d/swatch.png",
                checked = true,
                onCheckedChange = {}
            )
        }
    }
}
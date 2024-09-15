package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.ui.theme.ValolinkTheme

@Composable
fun RewardSelector(
    modifier: Modifier = Modifier,
    displayIcons: List<String>,
    onRewardSelected: (Int) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selected by remember {
        mutableIntStateOf(0)
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        shadowElevation = 3.dp,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { isExpanded = !isExpanded },
            ) {
                if (!isExpanded) Icon(Icons.Default.ArrowDropUp, "Expand icon")
                else Icon(Icons.Default.ArrowDropDown, "Collapse icon")
            }

            if (isExpanded) {
                displayIcons.forEachIndexed { index, icon ->
                    RewardSelectorButton(
                        displayIcon = icon,
                        checked = selected == index,
                        onCheckedChange = {
                            selected = index
                            isExpanded = false
                            onRewardSelected(index)
                        },
                    )
                }
            } else {
                RewardSelectorButton(
                    displayIcon = displayIcons[selected],
                    checked = true,
                    onCheckedChange = {
                        isExpanded = false
                        onRewardSelected(selected)
                    },
                )
            }
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
fun RewardSelectorPreview() {
    ValolinkTheme {
        Surface {
            RewardSelector(
                displayIcons = listOf("a", "a"),
                onRewardSelected = {}
            )
        }
    }
}
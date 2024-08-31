package dev.bittim.valolink.main.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.ui.theme.ValolinkTheme
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardType

enum class RewardTypeLabelStyle {
    DEFAULT,
    SMALL
}

@Composable
fun RewardTypeLabel(
    rewardType: RewardType,
    style: RewardTypeLabelStyle,
) {
    val textStyle = when (style) {
        RewardTypeLabelStyle.DEFAULT -> MaterialTheme.typography.titleMedium
        RewardTypeLabelStyle.SMALL   -> MaterialTheme.typography.labelMedium
    }

    val iconSize = when (style) {
        RewardTypeLabelStyle.DEFAULT -> 24.dp
        RewardTypeLabelStyle.SMALL   -> 20.dp
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = rewardType.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = rewardType.displayName,
            style = textStyle,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RewardTypeLabelPreview() {
    ValolinkTheme {
        Surface {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = RewardType.entries, itemContent = {
                    RewardTypeLabel(
                        rewardType = it,
                        style = RewardTypeLabelStyle.DEFAULT
                    )
                })
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SmallRewardTypeLabelPreview() {
    ValolinkTheme {
        Surface {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = RewardType.entries, itemContent = {
                    RewardTypeLabel(
                        rewardType = it,
                        style = RewardTypeLabelStyle.SMALL
                    )
                })
            }
        }
    }
}
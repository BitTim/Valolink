package dev.bittim.valolink.main.ui.screens.content.contracts.leveldetails.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.main.ui.components.ProgressCluster
import dev.bittim.valolink.main.ui.components.UnlockButton
import dev.bittim.valolink.main.ui.components.pulseAnimation

@Immutable
data class UnlockSectionData(
    val xpTotal: Int,
    val xpProgress: Int,
    val currencyIcon: String?,
    val price: Int,
    val isLocked: Boolean,
    val isOwned: Boolean
)

@Composable
fun UnlockSection(
    modifier: Modifier = Modifier,
    data: UnlockSectionData?,
    onUnlock: () -> Unit
) {
    val configuration = LocalConfiguration.current

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Unlock",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        Crossfade(
            modifier = Modifier.animateContentSize(),
            targetState = data,
            label = "Progress cluster crossfade"
        ) {
            if (it == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ButtonDefaults.MinHeight * configuration.fontScale)
                        .padding(1.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .pulseAnimation()
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (it.xpTotal >= 0) {
                        ProgressCluster(
                            modifier = Modifier.weight(2f),
                            progress = it.xpProgress,
                            total = it.xpTotal,
                            unit = "XP",
                            isMonochrome = false
                        )

                        Spacer(modifier = Modifier.width(24.dp))

                        if (it.currencyIcon != null) {
                            UnlockButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1.25f),
                                currencyIcon = it.currencyIcon,
                                price = it.price,
                                isPrimary = false,
                                isLocked = it.isLocked,
                                isOwned = it.isOwned,
                                onClick = onUnlock
                            )
                        }
                    } else {
                        if (it.currencyIcon != null) {
                            UnlockButton(
                                modifier = Modifier.fillMaxWidth(),
                                currencyIcon = it.currencyIcon,
                                price = it.price,
                                isPrimary = true,
                                isLocked = it.isLocked,
                                isOwned = it.isOwned,
                                onClick = onUnlock
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
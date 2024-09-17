package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.dialogs

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Level
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.components.PricedListItem

@Composable
fun RewardUnlockAlertDialog(
    levels: List<Level>,
    currencyDisplayIcon: String?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.LockOpen,
                contentDescription = null
            )
        },
        title = { Text(text = "Unlock Rewards") },
        text = {
            LazyColumn {
                item { Text(text = "You will unlock the following rewards:") }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                items(items = levels,
                      itemContent = { level ->
                          val rewards = level.rewards.map { it.relation }

                          if (rewards.isNotEmpty()) {
                              PricedListItem(
                                  rewards.joinToString("\n"),
                                  currencyDisplayIcon,
                                  level.doughCost,
                                  MaterialTheme.colorScheme.onSurface
                              )
                          }
                      }
                )

                item {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                }

                item {
                    PricedListItem(
                        "Total cost",
                        currencyDisplayIcon,
                        levels.sumOf { it.doughCost },
                        MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        onDismissRequest = onDismiss,

        confirmButton = {
            Button(onClick = {
                onDismiss()
                onConfirm()
            }) {
                Text(text = "Unlock")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}
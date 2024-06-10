package dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.dialogs

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.main.domain.model.game.contract.ChapterLevel
import dev.bittim.valolink.main.domain.model.game.contract.RewardRelation
import dev.bittim.valolink.main.ui.screens.content.contracts.agentdetails.components.PricedListItem

@Composable
fun RewardResetAlertDialog(
    rewards: List<Pair<RewardRelation, ChapterLevel>>,
    currencyDisplayIcon: String?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(icon = {
        Icon(
            imageVector = Icons.Default.Restore,
            contentDescription = null
        )
    },
                title = { Text(text = "Reset Rewards") },
                text = {
                    LazyColumn {
                        item { Text(text = "The following rewards will be reset:") }

                        item { Spacer(modifier = Modifier.height(16.dp)) }

                        items(items = rewards,
                              itemContent = {
                                  PricedListItem(
                                      it.first.displayName,
                                      currencyDisplayIcon,
                                      it.second.doughCost,
                                      MaterialTheme.colorScheme.onSurface
                                  )
                              })
                    }
                },
                onDismissRequest = onDismiss,

                confirmButton = {
                    Button(onClick = {
                        onDismiss()
                        onConfirm()
                    }) {
                        Text(text = "Reset")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                })
}
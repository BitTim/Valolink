/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       LevelUnlockAlertDialog.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   09.06.25, 18:52
 */

package dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.dialogs

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
import dev.bittim.valolink.content.domain.model.Currency
import dev.bittim.valolink.content.domain.model.contract.chapter.Level
import dev.bittim.valolink.content.ui.screens.content.contracts.agentdetails.components.PricedListItem
import dev.bittim.valolink.core.ui.theme.Spacing

@Composable
fun LevelUnlockAlertDialog(
    levels: List<Level>,
    currency: Currency?,
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

                item { Spacer(modifier = Modifier.height(Spacing.l)) }

                items(
                    items = levels,
                    itemContent = { level ->
                        val rewards = level.rewards.map { it.relation?.displayName }

                        if (rewards.isNotEmpty()) {
                            PricedListItem(
                                rewards.joinToString("\n"),
                                currency?.displayIcon,
                                when (currency?.uuid) {
                                    Currency.DOUGH_UUID -> level.doughCost
                                    Currency.VP_UUID -> level.vpCost
                                    else -> 0
                                },
                                MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )

                item {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.xs)
                    )
                }

                item {
                    PricedListItem(
                        "Total cost",
                        currency?.displayIcon,
                        levels.sumOf {
                            when (currency?.uuid) {
                                Currency.DOUGH_UUID -> it.doughCost
                                Currency.VP_UUID -> it.vpCost
                                else -> 0
                            }
                        },
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

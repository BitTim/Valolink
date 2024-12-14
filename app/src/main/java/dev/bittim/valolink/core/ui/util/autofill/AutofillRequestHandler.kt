/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       AutofillRequestHandler.kt
 Module:     Valolink.app.main
 Author:     Bagadesh
 Source:     https://medium.com/@bagadeshrp/compose-ui-textfield-autofill-6e2ac434e380
 Modified:   14.12.24, 14:35
 */

package dev.bittim.valolink.core.ui.util.autofill

import android.view.autofill.AutofillManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import dev.bittim.valolink.core.ui.util.extensions.toAndroidRect

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun autofillRequestHandler(
    autofillTypes: List<AutofillType> = listOf(),
    onFill: (String) -> Unit,
): AutofillHandler {
    val view = LocalView.current
    val context = LocalContext.current
    var filledRecently = remember { false }
    val autoFillNode = remember {
        AutofillNode(
            autofillTypes = autofillTypes,
            onFill = {
                filledRecently = true
                onFill(it)
            }
        )
    }

    val autofill = LocalAutofill.current
    LocalAutofillTree.current += autoFillNode

    return remember {
        object : AutofillHandler {
            val autofillManager = context.getSystemService(AutofillManager::class.java)

            override fun requestManual() {
                autofillManager.requestAutofill(
                    view,
                    autoFillNode.id,
                    autoFillNode.boundingBox?.toAndroidRect()
                        ?: error("BoundingBox is not provided yet")
                )
            }

            override fun requestVerifyManual() {
                if (filledRecently) {
                    filledRecently = false
                    requestManual()
                }
            }

            override val autoFill: Autofill?
                get() = autofill

            override val autoFillNode: AutofillNode
                get() = autoFillNode

            override fun request() {
                autofill?.requestAutofillForNode(autofillNode = autoFillNode)
            }

            override fun cancel() {
                autofill?.cancelAutofillForNode(autofillNode = autoFillNode)
            }

            override fun Modifier.fillBounds(): Modifier {
                return this.then(
                    Modifier.onGloballyPositioned {
                        autoFillNode.boundingBox = it.boundsInWindow()
                    }
                )
            }
        }
    }
}
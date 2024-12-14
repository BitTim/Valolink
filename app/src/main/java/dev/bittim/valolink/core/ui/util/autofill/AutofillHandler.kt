/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       AutofillHandler.kt
 Module:     Valolink.app.main
 Author:     Bagadesh
 Source:     https://medium.com/@bagadeshrp/compose-ui-textfield-autofill-6e2ac434e380
 Modified:   14.12.24, 14:35
 */

package dev.bittim.valolink.core.ui.util.autofill

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillNode

@OptIn(ExperimentalComposeUiApi::class)
interface AutofillHandler {
    val autoFill: Autofill?
    val autoFillNode: AutofillNode
    fun requestVerifyManual()
    fun requestManual()
    fun request()
    fun cancel()
    fun Modifier.fillBounds(): Modifier
}
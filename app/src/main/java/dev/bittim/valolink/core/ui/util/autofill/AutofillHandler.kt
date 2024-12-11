/*
Copyright (c) 2024 BitTim

Project:        Valolink
License:        GPLv3

File:           AutofillHandler.kt
Author:         Bagadesh (https://medium.com/@bagadeshrp/compose-ui-textfield-autofill-6e2ac434e380)
Created:        11.12.2024
Description:    Interface for handling autofill requests.
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
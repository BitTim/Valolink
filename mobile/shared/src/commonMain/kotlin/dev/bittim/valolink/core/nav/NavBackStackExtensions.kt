/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       NavBackStackExtensions.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 11:31
 */

package dev.bittim.valolink.core.nav

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateTo(destination: AppDestination) {
    add(destination)
}

fun NavBackStack<NavKey>.navigateBack() {
    removeLastOrNull()
}

fun NavBackStack<NavKey>.navigateToTopLevel(destination: AppDestination) {
    clear()
    add(destination)
}

fun NavBackStack<NavKey>.navigateAndClearUpTo(
    destination: AppDestination,
    inclusive: Boolean = false
) {
    val index = indexOfLast { it::class == destination::class }
    if (index != -1) {
        val removeFrom = if (inclusive) index else index + 1
        subList(removeFrom, size).clear()
    }
    add(destination)
}

fun NavBackStack<NavKey>.popUpTo(
    destination: AppDestination,
    inclusive: Boolean = false
) {
    val index = indexOfLast { it::class == destination::class }
    if (index != -1) {
        val removeFrom = if (inclusive) index else index + 1
        subList(removeFrom, size).clear()
    }
}

fun NavBackStack<NavKey>.replace(destination: AppDestination) {
    removeLastOrNull()
    add(destination)
}
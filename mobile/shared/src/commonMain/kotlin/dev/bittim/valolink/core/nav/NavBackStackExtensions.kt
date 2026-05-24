/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       NavBackStackExtensions.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 17:50
 */

package dev.bittim.valolink.core.nav

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateTo(destination: NavKey) {
    add(destination)
}

fun NavBackStack<NavKey>.navigateBack() {
    removeLastOrNull()
}

fun NavBackStack<NavKey>.navigateToTopLevel(destination: NavKey) {
    clear()
    add(destination)
}

fun NavBackStack<NavKey>.navigateAndClearUpTo(
    destination: NavKey,
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
    destination: NavKey,
    inclusive: Boolean = false
) {
    val index = indexOfLast { it::class == destination::class }
    if (index != -1) {
        val removeFrom = if (inclusive) index else index + 1
        subList(removeFrom, size).clear()
    }
}

fun NavBackStack<NavKey>.replace(destination: NavKey) {
    removeLastOrNull()
    add(destination)
}
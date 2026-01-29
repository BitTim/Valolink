/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserScopedKey.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.keys

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface UserScopedKey<K : UserScopedKey<K>> {
    val user: Uuid?
    fun withUser(user: Uuid): K
}
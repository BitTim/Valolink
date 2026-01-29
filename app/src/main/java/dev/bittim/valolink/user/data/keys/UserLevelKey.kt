/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserLevelKey.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.keys

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class UserLevelKey(
    override val user: Uuid? = null,
    val contract: Uuid,
    val level: Uuid? = null
) : UserScopedKey<UserLevelKey> {
    override fun withUser(user: Uuid) = copy(user = user)
}

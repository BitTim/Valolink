/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserAgentKey.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.keys

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class UserAgentKey(
    override val user: Uuid? = null,
    val agent: Uuid? = null
) : UserScopedKey<UserAgentKey> {
    override fun withUser(user: Uuid) = copy(user = user)
}

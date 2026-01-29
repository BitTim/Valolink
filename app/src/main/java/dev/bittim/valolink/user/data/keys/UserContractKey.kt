/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserContractKey.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.keys

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class UserContractKey(
    override val user: Uuid? = null,
    val contract: Uuid? = null
) : UserScopedKey<UserContractKey> {
    override fun withUser(user: Uuid) = copy(user = user)
}

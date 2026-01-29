/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AvatarBucket.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.storage.avatar

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface AvatarBucket {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun download(uid: Uuid, path: String?): ByteArray?

    @OptIn(ExperimentalUuidApi::class)
    suspend fun upload(uid: Uuid, rawData: ByteArray): String?
}
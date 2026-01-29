/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       AvatarSupabaseBucket.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.storage.avatar

import io.github.jan.supabase.storage.Storage
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AvatarSupabaseBucket(
    private val storage: Storage,
) : AvatarBucket {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun download(uid: Uuid, path: String?): ByteArray? {
        if (path == null) return null

        val bucket = storage.from("avatars")
        return bucket.downloadAuthenticated(path)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun upload(uid: Uuid, rawData: ByteArray): String? {
        val bucket = storage.from("avatars")
        val filename = "$uid.jpg"
        bucket.update(filename, rawData) { upsert = true }

        return filename
    }
}
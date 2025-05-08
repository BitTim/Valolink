/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Version.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.domain.model

data class Version(
    val manifestId: String,
    val branch: String,
    val version: String,
    val buildVersion: String,
    val engineVersion: String,
    val riotClientVersion: String,
    val riotClientBuild: String,
    val buildDate: String,
) {
    companion object {
        val EMPTY = Version(
            manifestId = "",
            branch = "",
            version = "",
            buildVersion = "",
            engineVersion = "",
            riotClientVersion = "",
            riotClientBuild = "",
            buildDate = ""
        )
    }
}

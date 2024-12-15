/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Content.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.domain.model.contract.content

import dev.bittim.valolink.content.domain.model.contract.chapter.Chapter

data class Content(
    val relation: ContentRelation?,
    val premiumVPCost: Int,
    val chapters: List<Chapter>,
)
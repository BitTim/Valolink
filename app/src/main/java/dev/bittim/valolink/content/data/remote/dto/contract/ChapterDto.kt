/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ChapterDto.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.remote.dto.contract

import dev.bittim.valolink.content.data.local.entity.contract.ChapterEntity

data class ChapterDto(
	val isEpilogue: Boolean,
	val levels: List<LevelDto>,
	val freeRewards: List<RewardDto>?,
) {
	fun toEntity(
		index: Int,
		version: String,
		contentUuid: String,
	): ChapterEntity {
		return ChapterEntity(
			contentUuid + "_" + index, contentUuid, version, isEpilogue
		)
	}
}
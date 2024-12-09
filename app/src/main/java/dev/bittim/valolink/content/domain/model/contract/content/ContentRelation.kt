package dev.bittim.valolink.content.domain.model.contract.content

import java.time.Instant

interface ContentRelation {
    val uuid: String
    val displayName: String
    val startTime: Instant?
    val endTime: Instant?

    fun calcRemainingDays(): Int?
}
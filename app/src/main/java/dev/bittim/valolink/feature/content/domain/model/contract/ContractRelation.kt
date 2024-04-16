package dev.bittim.valolink.feature.content.domain.model.contract

import java.time.ZonedDateTime

abstract class ContractRelation {
    abstract val displayName: String
    abstract val displayIcon: String?
    abstract val startTime: ZonedDateTime?
    abstract val endTime: ZonedDateTime?
    abstract val background: String?
    abstract val backgroundGradientColors: List<String>
}
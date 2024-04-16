package dev.bittim.valolink.feature.content.domain.model

import dev.bittim.valolink.feature.content.domain.model.contract.ContractRelation
import java.time.ZonedDateTime

data class Season(
    val uuid: String,
    override val displayName: String,
    override val startTime: ZonedDateTime,
    override val endTime: ZonedDateTime,

    override val displayIcon: String? = null,
    override val background: String? = null,
    override val backgroundGradientColors: List<String> = listOf()
) : ContractRelation()
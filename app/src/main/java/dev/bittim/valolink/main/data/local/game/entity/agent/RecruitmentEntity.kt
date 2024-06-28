package dev.bittim.valolink.main.data.local.game.entity.agent

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.agent.Agent
import dev.bittim.valolink.main.domain.model.game.agent.Recruitment
import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.game.contract.chapter.Chapter
import dev.bittim.valolink.main.domain.model.game.contract.chapter.ChapterLevel
import dev.bittim.valolink.main.domain.model.game.contract.content.Content
import dev.bittim.valolink.main.domain.model.game.contract.reward.Reward
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "AgentRecruitments",
    indices = [Index(
        value = ["uuid"],
        unique = true
    )]
)
data class RecruitmentEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val xp: Int,
    val useLevelVpCostOverride: Boolean,
    val levelVpCostOverride: Int,
    val startDate: String,
    val endDate: String,
) : GameEntity() {
    fun toType(): Recruitment {
        return Recruitment(
            uuid,
            xp,
            useLevelVpCostOverride,
            levelVpCostOverride,
            startDate,
            endDate
        )
    }

    fun toContract(agent: Agent): Contract {
        val agentWithTime = agent.copy(
            startTime = Instant.parse(startDate),
            endTime = Instant.parse(endDate)
        )

        return Contract(
            UUID.randomUUID().toString(),
            agent.displayName,
            useLevelVpCostOverride,
            levelVpCostOverride,
            Content(
                agentWithTime,
                -1,
                listOf(
                    Chapter(
                        listOf(
                            ChapterLevel(
                                xp,
                                1000,
                                false,
                                8000,
                                true,
                                Reward(
                                    "Agent",
                                    agent.uuid,
                                    1,
                                    false,
                                    null
                                )
                            )
                        ),
                        emptyList(),
                        false
                    )
                )
            )
        )
    }
}
package dev.bittim.valolink.content.data.local.entity.agent

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.GameEntity
import dev.bittim.valolink.content.domain.model.agent.Agent
import dev.bittim.valolink.content.domain.model.agent.Recruitment
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.content.domain.model.contract.chapter.Chapter
import dev.bittim.valolink.content.domain.model.contract.chapter.Level
import dev.bittim.valolink.content.domain.model.contract.content.Content
import dev.bittim.valolink.content.domain.model.contract.reward.Reward
import java.time.Instant

@Entity(
	tableName = "AgentRecruitments", indices = [Index(
		value = ["uuid"], unique = true
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
			uuid, xp, useLevelVpCostOverride, levelVpCostOverride, startDate, endDate
		)
	}

	fun toContract(agent: Agent): Contract {
		val agentWithTime = agent.copy(
			startTime = Instant.parse(startDate), endTime = Instant.parse(endDate)
		)

		return Contract(
			uuid = uuid,
			displayName = agent.displayName,
			useLevelVPCostOverride = useLevelVpCostOverride,
			levelVPCostOverride = levelVpCostOverride,
			content = Content(
				relation = agentWithTime, premiumVPCost = -1, chapters = listOf(
					Chapter(
						levels = listOf(
							element = Level(
								uuid = agent.uuid,
								dependency = null,
								name = "Level 1",
								xp = xp,
								vpCost = 0,
								isPurchasableWithVP = false,
								doughCost = 0,
								isPurchasableWithDough = false,
								rewards = listOf(
									Reward(
										rewardType = "Agent",
										rewardUuid = agent.uuid,
										amount = 1,
										isHighlighted = false,
										isFreeReward = false,
										relation = agent.toRewardRelation()
									)
								)
							)
						), isEpilogue = false
					)
				)
			)
		)
	}
}
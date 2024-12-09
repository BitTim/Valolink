package dev.bittim.valolink.main.domain.model.game

import dev.bittim.valolink.content.domain.model.agent.Agent
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

class AgentTest {
    @Test
    fun calcRemainingDays_returnsNull_whenNoStartOrEndTime() {
        assertEquals(
            null,
            Agent.EMPTY.copy(startTime = null, endTime = null).calcRemainingDays()
        )
    }

    @Test
    fun calcRemainingDays_returnsNull_whenEndTimeIsBeforeNow() {
        assertEquals(
            null,
            Agent.EMPTY.copy(
                startTime = Instant.now().minus(2, ChronoUnit.DAYS),
                endTime = Instant.now().minus(1, ChronoUnit.DAYS)
            ).calcRemainingDays()
        )
    }

    @Test
    fun calcRemainingDays_returnsNull_whenNowIsBeforeStartTime() {
        assertEquals(
            null,
            Agent.EMPTY.copy(
                startTime = Instant.now().plus(1, ChronoUnit.DAYS),
                endTime = Instant.now().plus(2, ChronoUnit.DAYS)
            ).calcRemainingDays()
        )
    }

    @Test
    fun calcRemainingDays_returnsCorrectValue_whenNowIsBetweenStartAndEndTime() {
        // Offset calculation by 10 Minutes to facilitate slower machines running the tests
        val now = Instant.now().plus(10, ChronoUnit.MINUTES)

        assertEquals(
            1,
            Agent.EMPTY.copy(
                startTime = now.minus(2, ChronoUnit.DAYS),
                endTime = now.plus(1, ChronoUnit.DAYS)
            ).calcRemainingDays()
        )
        assertEquals(
            0,
            Agent.EMPTY.copy(
                startTime = now.minus(2, ChronoUnit.DAYS),
                endTime = now
            ).calcRemainingDays()
        )
        assertEquals(
            1,
            Agent.EMPTY.copy(
                startTime = now.minus(2, ChronoUnit.DAYS),
                endTime = now.plus(1, ChronoUnit.DAYS).plus(12, ChronoUnit.HOURS)
            ).calcRemainingDays()
        )
        assertEquals(
            0,
            Agent.EMPTY.copy(
                startTime = now.minus(2, ChronoUnit.DAYS),
                endTime = now.plus(12, ChronoUnit.HOURS)
            ).calcRemainingDays()
        )
    }

    @Test
    fun toUserObj_returnsCorrectValue() {
        val agentUuid = "agent-" + UUID.randomUUID().toString()
        val uid = "uid-" + UUID.randomUUID().toString()

        val agent = Agent.EMPTY.copy(uuid = agentUuid)
        val userObj = agent.toUserObj(uid)

        assertEquals(agentUuid, userObj.agent)
        assertEquals(uid, userObj.user)
    }

    @Test
    fun toRewardRelation_returnsCorrectValue() {
        val uuid = UUID.randomUUID().toString()
        val displayName = "Agent Display Name"
        val displayIcon = "Agent Display Icon URL"
        val fullPortrait = "Agent Full Portrait URL"

        val agent = Agent.EMPTY.copy(
            uuid = uuid,
            displayName = displayName,
            displayIcon = displayIcon,
            fullPortrait = fullPortrait
        )
        val rewardRelation = agent.toRewardRelation()

        assertEquals(uuid, rewardRelation.uuid)
        assertEquals(RewardType.AGENT, rewardRelation.type)
        assertEquals(1, rewardRelation.amount)
        assertEquals(displayName, rewardRelation.displayName)
        assertEquals(displayIcon, rewardRelation.displayIcon)
        assertEquals(listOf(fullPortrait to null), rewardRelation.previewImages)
    }
}
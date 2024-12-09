package dev.bittim.valolink.main.domain.model.game

import dev.bittim.valolink.content.domain.model.buddy.Buddy
import dev.bittim.valolink.content.domain.model.buddy.BuddyLevel
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID
import kotlin.random.Random

class BuddyTest {
    @Test
    fun asRewardRelation_returnsFixedAmount_whenAmountIsLowerThanOne() {
        assertEquals(1, Buddy.EMPTY.asRewardRelation(0).amount)
        assertEquals(1, Buddy.EMPTY.asRewardRelation(-1).amount)
    }

    @Test
    fun asRewardRelation_returnsCorrectValue_whenLevelsIsEmpty() {
        val uuid = UUID.randomUUID().toString()
        val displayName = "Buddy Display Name"
        val displayIcon = "Buddy Display Icon URL"

        val levelDisplayIcon = "Buddy Level Display Icon URL"
        val levels = listOf(BuddyLevel.EMPTY.copy(displayIcon = levelDisplayIcon))

        val amount = Random.nextInt(1, 10)

        val emptyLevelsRewardRelation = Buddy.EMPTY.copy(
            uuid = uuid,
            displayName = displayName,
            displayIcon = displayIcon,
            levels = emptyList()
        ).asRewardRelation(amount)

        val filledLevelsRewardRelation = Buddy.EMPTY.copy(
            uuid = uuid,
            displayName = displayName,
            displayIcon = displayIcon,
            levels = levels
        ).asRewardRelation(amount)

        assertEquals(uuid, emptyLevelsRewardRelation.uuid)
        assertEquals(RewardType.BUDDY, emptyLevelsRewardRelation.type)
        assertEquals(amount, emptyLevelsRewardRelation.amount)
        assertEquals(displayName, emptyLevelsRewardRelation.displayName)
        assertEquals(listOf(displayIcon to null), emptyLevelsRewardRelation.previewImages)

        assertEquals(uuid, filledLevelsRewardRelation.uuid)
        assertEquals(RewardType.BUDDY, filledLevelsRewardRelation.type)
        assertEquals(amount, filledLevelsRewardRelation.amount)
        assertEquals(displayName, filledLevelsRewardRelation.displayName)
        assertEquals(listOf(levelDisplayIcon to null), filledLevelsRewardRelation.previewImages)
    }
}
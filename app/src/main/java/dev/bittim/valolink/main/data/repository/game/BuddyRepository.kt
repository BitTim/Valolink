package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.buddy.Buddy
import kotlinx.coroutines.flow.Flow

interface BuddyRepository : GameRepository<Buddy> {
    suspend fun getByLevelUuid(levelUuid: String): Flow<Buddy?>
}
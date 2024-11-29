package dev.bittim.valolink.core.data.repository.content.buddy

import dev.bittim.valolink.core.data.repository.content.ContentRepository
import dev.bittim.valolink.main.domain.model.game.buddy.Buddy
import kotlinx.coroutines.flow.Flow

interface BuddyRepository : ContentRepository<Buddy> {
    suspend fun getByLevelUuid(levelUuid: String): Flow<Buddy?>
}
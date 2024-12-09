package dev.bittim.valolink.content.data.repository.buddy

import dev.bittim.valolink.content.data.repository.ContentRepository
import dev.bittim.valolink.content.domain.model.buddy.Buddy
import kotlinx.coroutines.flow.Flow

interface BuddyRepository : ContentRepository<Buddy> {
    suspend fun getByLevelUuid(levelUuid: String): Flow<Buddy?>
}
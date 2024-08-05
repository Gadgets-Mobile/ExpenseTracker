package domain.repo

import data.models.Entry
import kotlinx.coroutines.flow.Flow

interface AmountRepository {
    fun getAllEntries(): List<Entry>

    suspend fun upsertData(entry: Entry)

    suspend fun deleteById(id: Long)

    suspend fun getEntryById(id: Long): Entry?
}

expect fun getAmountRepository(): AmountRepository
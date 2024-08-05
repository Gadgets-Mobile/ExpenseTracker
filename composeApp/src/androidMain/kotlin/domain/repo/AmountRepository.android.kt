package domain.repo

import data.models.Entry
import dummyEntries

object AndroidAmountRepository : AmountRepository {
    private val _entries = mutableListOf<Entry>().apply {
        addAll(dummyEntries)
    }

    override suspend fun upsertData(entry: Entry) {
        if (_entries.find { it.id == entry.id } == null) {
            _entries.add(entry)
        } else {
            _entries[_entries.indexOfFirst { it.id == entry.id }] = entry
        }
    }

    override suspend fun deleteById(id: Long) {
        _entries.removeIf { it.id == id }
    }

    override fun getAllEntries() = _entries.toList()

    override suspend fun getEntryById(id: Long) = _entries.find { it.id == id }
}

actual fun getAmountRepository(): AmountRepository = AndroidAmountRepository
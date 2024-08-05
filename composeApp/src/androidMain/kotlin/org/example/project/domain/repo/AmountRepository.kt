package org.example.project.domain.repo

import data.models.Entry
import org.example.project.domain.local.dao.AmountDao
import org.example.project.domain.local.entity.AmountEntity

class AmountRepository(
    private val dao: AmountDao,
) {
    suspend fun upsertData(entry: Entry) {
        dao.upsertAmountEntity(entry.toEntity())
    }

    suspend fun deleteById(id:Long) {
        dao.deleteById(id)
    }

    fun getAllEntries() = dao.getAllEntries()
    suspend fun getEntryById(id: Long) = dao.getEntryById(id)
}
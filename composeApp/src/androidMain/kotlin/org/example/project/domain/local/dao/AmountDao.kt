package org.example.project.domain.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.example.project.domain.AMOUNT_TABLE_NAME
import org.example.project.domain.local.entity.AmountEntity

/**
 * @author bala-3019
 * @Created-on 15/03/24
 */

@Dao
interface AmountDao {
    @Upsert
    suspend fun upsertAmountEntity(item: AmountEntity)

    @Query("SELECT * FROM $AMOUNT_TABLE_NAME")
    fun getAllEntries(): Flow<List<AmountEntity>>

    @Query("SELECT * FROM $AMOUNT_TABLE_NAME WHERE id = :id")
    suspend fun getEntryById(id: Long): AmountEntity?

    @Query("DELETE FROM $AMOUNT_TABLE_NAME WHERE id = :id")
    suspend fun deleteById(id: Long)
}
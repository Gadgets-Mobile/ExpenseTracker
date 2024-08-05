package org.example.project.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.project.domain.local.dao.AmountDao
import org.example.project.domain.local.entity.AmountEntity

@Database(
    entities = [AmountEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class GadgetsAIDatabase : RoomDatabase() {
    internal abstract fun amountDao(): AmountDao
}
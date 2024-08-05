package org.example.project.domain.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import data.enums.Category
import data.enums.Type
import org.example.project.domain.AMOUNT_TABLE_NAME

@Entity(tableName = AMOUNT_TABLE_NAME)
data class AmountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val title: String,
    val date: String,
    val amount: Double,
    val type: Type,
    val category: Category,
)
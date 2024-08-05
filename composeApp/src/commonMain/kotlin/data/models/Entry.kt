package data.models

import data.enums.Category
import data.enums.Type

data class Entry(
    val id: Int,
    val title: String,
    val amount: Long,
    val date: String,
    val type: Type,
    val category: Category,
)
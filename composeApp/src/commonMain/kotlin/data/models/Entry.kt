package data.models

import data.enums.Category
import data.enums.Type

data class Entry(
    val id: Long? = null,
    val title: String = "",
    val amount: Int = 0,
    val date: String = "",
    val type: Type = Type.EXPENSE,
    val category: Category = Category.OTHER,
)
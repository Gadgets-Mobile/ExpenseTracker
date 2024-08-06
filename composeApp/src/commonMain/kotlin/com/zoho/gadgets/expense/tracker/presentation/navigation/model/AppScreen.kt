package com.zoho.gadgets.expense.tracker.presentation.navigation.model

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.zoho.gadgets.expense.tracker.presentation.navigation.enums.Purpose

sealed class AppScreen(
    val route: String,
    val destination: String = route,
    val args: List<NamedNavArgument> = emptyList(),
) {
    data object Dashboard : AppScreen("dashboard")
    data class AddEditEntry(val purpose: Purpose, val id: Long? = null) : AppScreen(
        route = "add_edit_entry?id={id}&purpose={purpose}",
        destination = "add_edit_entry?id=${id}&purpose=${purpose.ordinal}",
        args = listOf(
            navArgument("id") {
                type = NavType.LongType
                defaultValue = -1
            },
            navArgument("purpose") {
                type = NavType.IntType
            }
        )
    )

    data object RecommendationAppScreen : AppScreen("recommendation")
    data object DebtRecommendationAppScreen : AppScreen("debt_recommendation")
}
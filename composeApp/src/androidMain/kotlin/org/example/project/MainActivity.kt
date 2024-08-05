package org.example.project

import App
import EntryItem
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import data.enums.Category
import data.enums.Type
import data.models.Entry
import viewmodel.AddEditEntryViewModel
import viewmodel.DashboardViewModel
import viewmodel.RecommendationViewmodel

class MainActivity : ComponentActivity() {

    private val dashboardViewModel by viewModels<DashboardViewModel>()
    private val addEditEntryViewModel by viewModels<AddEditEntryViewModel>()
    private val recommendationViewmodel by viewModels<RecommendationViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(dashboardViewModel, addEditEntryViewModel, recommendationViewmodel)
        }
    }
}

@Preview
@Composable
fun EntryItemPreview() {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            EntryItem(
                modifier = Modifier.fillMaxSize(),
                data = Entry(
                    id = 1,
                    title = "Lunch",
                    amount = 10,
                    date = "2021-09-01",
                    type = Type.EXPENSE,
                    category = Category.FOOD,
                ),
            )
        }
    }
}
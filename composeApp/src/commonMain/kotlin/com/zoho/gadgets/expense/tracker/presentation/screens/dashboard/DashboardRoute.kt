package com.zoho.gadgets.expense.tracker.presentation.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zoho.gadgets.expense.tracker.di.koinViewModel
import com.zoho.gadgets.expense.tracker.presentation.components.TransactionItem
import com.zoho.gadgets.expense.tracker.presentation.navigation.enums.Purpose
import com.zoho.gadgets.expense.tracker.presentation.navigation.enums.Purpose.ADD
import com.zoho.gadgets.expense.tracker.presentation.navigation.model.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardRoute(
    navController: NavController,
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val entries by viewModel.entries.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllEntries()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Tracker") },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(AppScreen.RecommendationAppScreen.destination)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = {
                            navController.navigate(AppScreen.DebtRecommendationAppScreen.destination)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AppScreen.AddEditEntry(ADD).destination)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).consumeWindowInsets(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(entries) { entry ->
                TransactionItem(
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate(
                            AppScreen.AddEditEntry(
                                purpose = Purpose.EDIT,
                                id = entry.id!!
                            ).destination
                        )
                    }),
                    data = entry
                )
            }
        }
    }
}
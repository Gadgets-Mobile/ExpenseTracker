package com.zoho.gadgets.expense.tracker.presentation.screens.purchaseRecommendation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zoho.gadgets.expense.tracker.di.koinViewModel
import com.zoho.gadgets.expense.tracker.domain.enums.PurchaseRecommendationHeaderType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseRecommendationScreenRoute(
    navController: NavController,
    viewModel: PurchaseRecommendationViewmodel = koinViewModel(),
) {
    var salary by remember { mutableStateOf("10000") }
    var productRate by remember { mutableStateOf("8000") }
    val expenses by viewModel.expense.collectAsState()
    var emiNeeded by remember { mutableStateOf(false) }

    val loading by viewModel.loading.collectAsState()
    val recommendation by viewModel.recommendation.collectAsState()

    DisposableEffect(Unit) {
        viewModel.calculateTotalExpense()
        onDispose {
            viewModel.clearRecommendation()
        }
    }

    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Can you afford it?") },
                scrollBehavior = topAppBarScrollBehavior,
                actions = {
                    IconButton(
                        enabled = !loading,
                        onClick = {
                            viewModel.generateRecommendation(
                                income = salary.toInt(),
                                productValue = productRate.toInt(),
                                totalExpenses = expenses,
                                isEmiMandatory = emiNeeded
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        enabled = !loading,
                        onClick = navController::navigateUp
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = salary,
                onValueChange = { salary = it },
                label = { Text("Monthly Salary") },
                enabled = !loading,
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = "$${expenses}",
                onValueChange = { },
                label = { Text("Monthly Expenses") },
                readOnly = true,
                enabled = !loading,
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = productRate,
                onValueChange = { productRate = it },
                label = { Text("Product Rate") },
                enabled = !loading,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Need to buy immediately in EMI?")
                Switch(
                    checked = emiNeeded,
                    onCheckedChange = { emiNeeded = !emiNeeded },
                    enabled = !loading,
                )
            }

            if (recommendation.isNotEmpty() && !loading) {
                PurchaseRecommendationHeaderType.entries.forEach { key ->
                    val response = recommendation.find { key == it.key }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = response?.key?.name ?: "",
                                style = MaterialTheme.typography.headlineMedium,
                            )
                            HorizontalDivider(modifier = Modifier.fillMaxWidth())
                            Text(
                                text = "Recommendation",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = response?.recommendation ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = "Reason",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = response?.reason ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }
    }
}
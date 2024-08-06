package com.zoho.gadgets.expense.tracker.presentation.screens.purchaseRecommendation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.zoho.gadgets.expense.tracker.domain.enums.PurchaseRecommendationHeaderType
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PurchaseRecommendationScreenRoute(
    navController: NavController,
    viewModel: PurchaseRecommendationViewmodel = koinViewModel(),
) {
    var salary by remember { mutableStateOf("10000") }
    var productRate by remember { mutableStateOf("8000") }
    val expenses by viewModel.expense.collectAsStateWithLifecycle()
    var emiNeeded by remember { mutableStateOf(false) }

    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val recommendation by viewModel.recommendation.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        viewModel.calculateTotalExpense()
        onDispose {
            viewModel.clearRecommendation()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Can you afford it?") },
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
    ) {
        Column(
            modifier = Modifier
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
                        elevation = 1.dp,
                        border = BorderStroke(1.dp, Color.Gray),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = response?.key?.name ?: "",
                                style = MaterialTheme.typography.h6,
                            )
                            Divider(modifier = Modifier.fillMaxWidth())
                            Text(
                                text = "Recommendation",
                                style = MaterialTheme.typography.subtitle2,
                            )
                            Text(
                                text = response?.recommendation ?: "",
                                style = MaterialTheme.typography.body2,
                            )
                            Text(
                                text = "Reason",
                                style = MaterialTheme.typography.subtitle2,
                            )
                            Text(
                                text = response?.reason ?: "",
                                style = MaterialTheme.typography.body2,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }
    }
}
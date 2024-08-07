package com.zoho.gadgets.expense.tracker.presentation.screens.debtRecommendation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtRecommendationScreenRoute(
    navController: NavController,
    viewModel: DebtRecommendationViewmodel = koinViewModel(),
) {
    var monthlyEMI by remember { mutableStateOf("10000") }
    var totalLoan by remember { mutableStateOf("2000000") }
    var salary by remember { mutableStateOf("20000") }
    var paidPeriod by remember { mutableStateOf("2 months") }
    var totalPaymentPeriod by remember { mutableStateOf("20 years") }
    var interestRate by remember { mutableStateOf("8.5") }

    val expenses by viewModel.expense.collectAsState()
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
                title = { Text("How fast can I repay my debt?") },
                scrollBehavior = topAppBarScrollBehavior,
                actions = {
                    IconButton(
                        enabled = !loading,
                        onClick = {
                            viewModel.generateRecommendation(
                                monthlyEMI = monthlyEMI,
                                totalLoan = totalLoan,
                                salary = salary,
                                paidPeriod = paidPeriod,
                                totalPaymentPeriod = totalPaymentPeriod,
                                interestRate = interestRate,
                                expenses = expenses
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
                value = monthlyEMI,
                onValueChange = { monthlyEMI = it },
                label = { Text("Monthly EMI") },
                enabled = !loading,
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = totalLoan,
                onValueChange = { totalLoan = it },
                label = { Text("Total Loan") },
                enabled = !loading,
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = salary,
                onValueChange = { salary = it },
                label = { Text("Monthly Salary") },
                enabled = !loading,
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = paidPeriod,
                onValueChange = { paidPeriod = it },
                label = { Text("Paid Tenure") },
                enabled = !loading,
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = totalPaymentPeriod,
                onValueChange = { totalPaymentPeriod = it },
                label = { Text("Total Tenure") },
                enabled = !loading,
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = interestRate,
                onValueChange = { interestRate = it },
                label = { Text("Interest") },
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


            if (recommendation.isNotEmpty() && !loading) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "Suggestions",
                        style = MaterialTheme.typography.titleLarge,
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(recommendation) { item ->
                            Card(
                                modifier = Modifier.fillParentMaxWidth(0.8f),
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Text(
                                        text = "Description",
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                    Text(
                                        text = item.description,
                                        style = MaterialTheme.typography.bodyLarge,
                                    )
                                    Text(
                                        text = "Action",
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                    Text(
                                        text = item.action,
                                        style = MaterialTheme.typography.bodyLarge,
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
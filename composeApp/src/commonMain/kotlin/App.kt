import Purpose.ADD
import Purpose.EDIT
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import data.ResponseKey
import data.enums.Category
import data.enums.Category.BILLS
import data.enums.Category.LOANS
import data.enums.Category.ENTERTAINMENT
import data.enums.Category.FOOD
import data.enums.Category.OTHER
import data.enums.Category.SHOPPING
import data.enums.Category.TRANSPORT
import data.enums.Type
import data.models.Entry
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.ui.tooling.preview.Preview
import viewmodel.AddEditEntryViewModel
import viewmodel.DashboardViewModel
import viewmodel.RecommendationViewmodel

enum class Purpose {
    ADD,
    EDIT
}

sealed class Screen(
    val route: String,
    val destination: String = route,
    val args: List<NamedNavArgument> = emptyList(),
) {
    data object Dashboard : Screen("dashboard")
    data class AddEditEntry(val purpose: Purpose, val id: Long? = null) : Screen(
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

    data object RecommendationScreen : Screen("recommendation")
    data object DebtRecommendationScreen : Screen("debt_recommendation")
}

@Composable
@Preview
fun App(
    dashboardViewModel: DashboardViewModel = DashboardViewModel(),
    addEditEntryViewModel: AddEditEntryViewModel = AddEditEntryViewModel(),
    recommendationViewmodel: RecommendationViewmodel = RecommendationViewmodel(),
) {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route
        ) {
            composable(
                route = Screen.Dashboard.route,
                arguments = Screen.Dashboard.args
            ) {
                DashboardRoute(navController, dashboardViewModel)
            }
            composable(
                route = Screen.RecommendationScreen.route,
                arguments = Screen.RecommendationScreen.args
            ) {
                RecommendationScreenRoute(navController, recommendationViewmodel)
            }
            composable(
                route = Screen.DebtRecommendationScreen.route,
                arguments = Screen.DebtRecommendationScreen.args
            ) {
                DebtRecommendationScreenRoute(navController, recommendationViewmodel)
            }
            composable(
                route = Screen.AddEditEntry(ADD).route,
                arguments = Screen.AddEditEntry(ADD).args
            ) { backStack ->
                val ordinal = backStack.arguments?.getInt("purpose") ?: 0
                val id = backStack.arguments?.getLong("id")
                val purpose = Purpose.entries[ordinal]
                AddEditEntryRoute(navController, purpose, id, addEditEntryViewModel)
            }
        }
    }
}

@Composable
fun DashboardRoute(
    navController: NavController,
    viewModel: DashboardViewModel,
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
                            navController.navigate(Screen.RecommendationScreen.destination)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.DebtRecommendationScreen.destination)
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
                    navController.navigate(Screen.AddEditEntry(ADD).destination)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) {
        LazyColumn {
            items(entries) { entry ->
                EntryItem(
                    modifier = Modifier.clickable(onClick = {
                        //navController.navigate(Screen.AddEditEntry(EDIT, entry.id!!).destination)
                    }),
                    data = entry
                )
            }
        }
    }
}

@Composable
fun RecommendationScreenRoute(
    navController: NavController,
    viewModel: RecommendationViewmodel,
) {
    var salary by remember { mutableStateOf("10000") }
    var productRate by remember { mutableStateOf("8000") }
    val expenses by viewModel.expense.collectAsState()
    var emiNeeded by remember { mutableStateOf(false) }

    val loading by viewModel.loading.collectAsState()

    val recommendation by viewModel.recommendation.collectAsState()

    DisposableEffect(Unit){
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
                ResponseKey.entries.forEach { key ->
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

@Composable
fun DebtRecommendationScreenRoute(
    navController: NavController,
    viewModel: RecommendationViewmodel,
) {
    var monthlyEMI by remember { mutableStateOf("10000") }
    var totalLoan by remember { mutableStateOf("2000000") }
    var salary by remember { mutableStateOf("20000") }
    var paidPeriod by remember { mutableStateOf("2 months") }
    var totalPaymentPeriod by remember { mutableStateOf("20 years") }
    var interestRate by remember { mutableStateOf("8.5") }

    val expenses by viewModel.expense.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val recommendation by viewModel.debtRecommendation.collectAsState()

    DisposableEffect(Unit){
        viewModel.calculateTotalExpense()
        onDispose {
            viewModel.clearRecommendation()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("How fast can I repay my dept?") },
                actions = {
                    IconButton(
                        enabled = !loading,
                        onClick = {
                            viewModel.generateRecommendationForDebt(
                                monthlyEMI,
                                totalLoan,
                                salary,
                                paidPeriod,
                                totalPaymentPeriod,
                                interestRate,
                                expenses
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
                        style = MaterialTheme.typography.h6,
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(recommendation) { item ->
                            Card(
                                modifier = Modifier.fillParentMaxWidth(0.8f),
                                elevation = 1.dp,
                                border = BorderStroke(1.dp, Color.Gray),
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Text(
                                        text = "Description",
                                        style = MaterialTheme.typography.subtitle2,
                                    )
                                    Text(
                                        text = item.description,
                                        style = MaterialTheme.typography.body2,
                                    )
                                    Text(
                                        text = "Action",
                                        style = MaterialTheme.typography.subtitle2,
                                    )
                                    Text(
                                        text = item.action,
                                        style = MaterialTheme.typography.body2,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddEditEntryRoute(
    navController: NavController,
    purpose: Purpose,
    id: Long? = null,
    viewModel: AddEditEntryViewModel,
) {
    var entry by remember { mutableStateOf(Entry()) }
    var amount by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(id) {
        if (id != null && purpose == EDIT) {
            viewModel.getEntryById(id)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.entry.collectLatest {
            entry = it
            amount = it.amount.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = when (purpose) {
                        ADD -> "Add Entry"
                        EDIT -> "Edit Entry"
                    }
                    Text(title)
                },
                navigationIcon = {
                    IconButton(onClick = navController::navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    if (purpose == EDIT) {
                        IconButton(onClick = {
                            viewModel.deleteById(entry.id!!)
                            navController.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null
                            )
                        }
                    }
                    IconButton(onClick = {
                        runCatching {
                            viewModel.upsertData(entry.copy(amount = amount.toInt()))
                            navController.navigateUp()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = entry.title,
                onValueChange = { entry = entry.copy(title = it) },
                label = { Text("Title") }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = amount,
                onValueChange = { amount = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Amount") },
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = entry.date,
                onValueChange = { entry = entry.copy(date = it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Date") },
                placeholder = { Text("DD-MM-YYYY") }
            )

            var categoryExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                modifier = Modifier.fillMaxWidth(),
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded },
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value = entry.category.name,
                    onValueChange = {},
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                )
                ExposedDropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false },
                ) {
                    Category.entries.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                entry = entry.copy(category = selectionOption)
                                categoryExpanded = false
                            },
                        ) {
                            Text(selectionOption.name)
                        }
                    }
                }
            }

            var typeExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                modifier = Modifier.fillMaxWidth(),
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = !typeExpanded },
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value = entry.type.name,
                    onValueChange = {},
                    label = { Text("Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                )
                ExposedDropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false },
                ) {
                    Type.entries.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                entry = entry.copy(type = selectionOption)
                                typeExpanded = false
                            },
                        ) {
                            Text(selectionOption.name)
                        }
                    }
                }
            }

        }

    }
}

val dummyEntries = listOf(
    Entry(
        id = 1,
        title = "Lunch",
        amount = 10,
        date = "2021-09-01",
        type = Type.EXPENSE,
        category = Category.FOOD,
    ),
    Entry(
        id = 2,
        title = "Salary",
        amount = 1000,
        date = "2021-09-01",
        type = Type.INCOME,
        category = Category.OTHER,
    ),
    Entry(
        id = 3,
        title = "Transport",
        amount = 20,
        date = "2021-09-01",
        type = Type.EXPENSE,
        category = Category.TRANSPORT,
    ),
    Entry(
        id = 4,
        title = "Shopping",
        amount = 50,
        date = "2021-09-01",
        type = Type.EXPENSE,
        category = Category.SHOPPING,
    ),
    Entry(
        id = 5,
        title = "Electricity",
        amount = 100,
        date = "2021-09-01",
        type = Type.EXPENSE,
        category = Category.BILLS,
    ),
    Entry(
        id = 6,
        title = "Movie",
        amount = 20,
        date = "2021-09-01",
        type = Type.EXPENSE,
        category = Category.ENTERTAINMENT,
    ),
    Entry(
        id = 7,
        title = "Gift",
        amount = 30,
        date = "2021-09-01",
        type = Type.EXPENSE,
        category = Category.OTHER,
    ),
    Entry(
        id = 8,
        title = "Housing Loan",
        amount = 200,
        date = "2021-09-01",
        type = Type.EXPENSE,
        category = Category.LOANS,
    ),
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EntryItem(
    data: Entry,
    modifier: Modifier = Modifier,
) {
    val emoji = when (data.category) {
        FOOD -> "🍔"
        TRANSPORT -> "🚗"
        SHOPPING -> "🛍"
        ENTERTAINMENT -> "🎉"
        BILLS -> "💡"
        LOANS -> "💰"
        OTHER -> "📦"
    }

    ListItem(
        modifier = modifier,
        icon = {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    color = Color.White,
                    fontSize = 24.sp
                )
            }
        },
        text = {
            Text(data.title)
        },
        secondaryText = {
            Text(data.category.name.lowercase().replaceFirstChar { it.uppercase() })
        },
        trailing = {
            val symbol = when (data.type) {
                Type.INCOME -> "+"
                Type.EXPENSE -> "-"
            }

            val textColor = when (data.type) {
                Type.INCOME -> Color(0xFF50C878)
                Type.EXPENSE -> Color(0xFFFF2525)
            }

            Text(
                "$symbol$${data.amount}",
                style = MaterialTheme.typography.body1,
                color = textColor
            )
        },
    )
}
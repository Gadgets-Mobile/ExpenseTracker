import data.enums.Category.BILLS
import data.enums.Category.ENTERTAINMENT
import data.enums.Category.FOOD
import data.enums.Category.OTHER
import data.enums.Category.SHOPPING
import data.enums.Category.TRANSPORT
import Purpose.ADD
import Purpose.EDIT
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import data.enums.Category
import data.enums.Type
import data.models.Entry
import org.jetbrains.compose.ui.tooling.preview.Preview

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
    data class AddEditEntry(val purpose: Purpose) : Screen(
        route = "add_edit_entry?purpose={purpose}",
        destination = "add_edit_entry?purpose=${purpose.ordinal}",
        args = listOf(
            navArgument("purpose") {
                type = NavType.IntType
            }
        )
    )

    data object RecommendationScreen : Screen("recommendation")
}

@Composable
@Preview
fun App() {
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
                DashboardRoute(navController)
            }
            composable(
                route = Screen.RecommendationScreen.route,
                arguments = Screen.RecommendationScreen.args
            ) {
                RecommendationScreenRoute(navController)
            }
            composable(
                route = Screen.AddEditEntry(ADD).route,
                arguments = Screen.AddEditEntry(ADD).args
            ) { backStack ->
                val ordinal = backStack.arguments?.getInt("purpose") ?: 0
                val purpose = Purpose.entries[ordinal]
                AddEditEntryRoute(navController, purpose)
            }
        }
    }
}

@Composable
fun DashboardRoute(
    navController: NavController,
) {
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
                            imageVector = Icons.Default.BubbleChart,
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
            items(dummyEntries) { entry ->
                EntryItem(data = entry)
            }
        }
    }
}

@Composable
fun RecommendationScreenRoute(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Can you afford it?") },
                navigationIcon = {
                    IconButton(onClick = navController::navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
    ) {

    }
}

@Composable
fun AddEditEntryRoute(
    navController: NavController,
    purpose: Purpose,
) {
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
                }
            )
        },
    ) {

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
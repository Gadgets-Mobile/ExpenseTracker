
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zoho.gadgets.expense.tracker.presentation.navigation.enums.Purpose
import com.zoho.gadgets.expense.tracker.presentation.navigation.enums.Purpose.ADD
import com.zoho.gadgets.expense.tracker.presentation.navigation.model.AppScreen
import com.zoho.gadgets.expense.tracker.presentation.screens.addEditEntry.AddEditEntryRoute
import com.zoho.gadgets.expense.tracker.presentation.screens.dashboard.DashboardRoute
import com.zoho.gadgets.expense.tracker.presentation.screens.debtRecommendation.DebtRecommendationScreenRoute
import com.zoho.gadgets.expense.tracker.presentation.screens.purchaseRecommendation.PurchaseRecommendationScreenRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    MaterialTheme {
        KoinContext {
            Surface {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = AppScreen.Dashboard.route
                ) {
                    composable(
                        route = AppScreen.Dashboard.route,
                        arguments = AppScreen.Dashboard.args
                    ) {
                        DashboardRoute(navController)
                    }
                    composable(
                        route = AppScreen.RecommendationAppScreen.route,
                        arguments = AppScreen.RecommendationAppScreen.args
                    ) {
                        PurchaseRecommendationScreenRoute(navController)
                    }
                    composable(
                        route = AppScreen.DebtRecommendationAppScreen.route,
                        arguments = AppScreen.DebtRecommendationAppScreen.args
                    ) {
                        DebtRecommendationScreenRoute(navController)
                    }
                    composable(
                        route = AppScreen.AddEditEntry(ADD).route,
                        arguments = AppScreen.AddEditEntry(ADD).args
                    ) { backStack ->
                        val ordinal = backStack.arguments?.getInt("purpose") ?: 0
                        val id = backStack.arguments?.getLong("id")
                        val purpose = Purpose.entries[ordinal]
                        AddEditEntryRoute(navController, purpose, id)
                    }
                }
            }
        }
    }
}







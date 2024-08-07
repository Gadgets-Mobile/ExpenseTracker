import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.zoho.gadgets.expense.tracker.di.appModules
import org.koin.compose.KoinApplication

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Expense Tracker",
    ) {
        KoinApplication(
            application = { modules(appModules()) },
            content = { App() },
        )
    }
}
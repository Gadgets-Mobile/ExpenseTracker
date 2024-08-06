
import org.koin.core.context.startKoin
import org.koin.dsl.module
import com.zoho.gadgets.expense.tracker.data.local.DatabaseDriverFactory
import com.zoho.gadgets.expense.tracker.di.commonModules
import com.zoho.gadgets.expense.tracker.data.local.IOSDatabaseDriverFactory


class KoinHelper {

    fun initKoin() {
        startKoin {
            modules(commonModules() + platformModule)
        }
    }

}

private val platformModule = module {
    single<DatabaseDriverFactory> {
        IOSDatabaseDriverFactory()
    }
}
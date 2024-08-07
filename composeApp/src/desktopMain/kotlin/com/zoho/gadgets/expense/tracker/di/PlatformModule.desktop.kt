package com.zoho.gadgets.expense.tracker.di

import com.zoho.gadgets.expense.tracker.data.local.DatabaseDriverFactory
import com.zoho.gadgets.expense.tracker.data.local.JVMDatabaseDriverFactory
import org.koin.dsl.module

actual val platformModule = module {
    single<DatabaseDriverFactory> {
        JVMDatabaseDriverFactory()
    }
}
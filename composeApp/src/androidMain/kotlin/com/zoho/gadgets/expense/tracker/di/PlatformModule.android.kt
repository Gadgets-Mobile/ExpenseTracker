package com.zoho.gadgets.expense.tracker.di

import com.zoho.gadgets.expense.tracker.data.local.AndroidDatabaseDriverFactory
import com.zoho.gadgets.expense.tracker.data.local.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<DatabaseDriverFactory> {
        AndroidDatabaseDriverFactory(androidContext())
    }
}
package com.zoho.gadgets.expense.tracker

import android.app.Application
import com.zoho.gadgets.expense.tracker.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ExpenseTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ExpenseTrackerApplication)
            androidLogger()
            modules(appModules())
        }
    }

}
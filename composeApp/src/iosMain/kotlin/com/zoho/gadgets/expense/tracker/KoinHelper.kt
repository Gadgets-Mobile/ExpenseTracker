package com.zoho.gadgets.expense.tracker

import com.zoho.gadgets.expense.tracker.di.commonModules
import com.zoho.gadgets.expense.tracker.di.platformModule
import org.koin.core.context.startKoin

class KoinHelper {

    fun initKoin() {
        startKoin {
            modules(commonModules() + platformModule)
        }
    }

}
package com.zoho.gadgets.expense.tracker

import App
import androidx.compose.ui.window.ComposeUIViewController
import com.zoho.gadgets.expense.tracker.di.appModules
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin { modules(appModules()) }
    }
) { App() }
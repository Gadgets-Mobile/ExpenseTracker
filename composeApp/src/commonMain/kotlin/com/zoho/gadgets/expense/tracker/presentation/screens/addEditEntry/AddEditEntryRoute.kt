package com.zoho.gadgets.expense.tracker.presentation.screens.addEditEntry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zoho.gadgets.expense.tracker.di.koinViewModel
import com.zoho.gadgets.expense.tracker.domain.enums.Category
import com.zoho.gadgets.expense.tracker.domain.enums.Type
import com.zoho.gadgets.expense.tracker.presentation.navigation.enums.Purpose
import com.zoho.gadgets.expense.tracker.presentation.navigation.enums.Purpose.ADD
import com.zoho.gadgets.expense.tracker.presentation.navigation.enums.Purpose.EDIT


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEntryRoute(
    navController: NavController,
    purpose: Purpose,
    id: Long? = null,
    viewModel: AddEditEntryViewModel = koinViewModel(),
) {
    val transaction by viewModel.transaction.collectAsState()

    LaunchedEffect(Unit) {
        if (id != null && purpose == EDIT) {
            viewModel.getEntryById(id)
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
                            viewModel.deleteById(transaction.id!!)
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
                            viewModel.saveChanges()
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
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = transaction.title,
                onValueChange = { viewModel.updateEntry(transaction.copy(title = it)) },
                label = { Text("Title") }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = if (transaction.amount == 0) "" else transaction.amount.toString(),
                onValueChange = {
                    if (it.isBlank()) {
                        viewModel.updateEntry(transaction.copy(amount = 0))
                        return@TextField
                    }
                    runCatching {
                        viewModel.updateEntry(transaction.copy(amount = it.toInt()))
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Amount") },
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = transaction.date,
                onValueChange = { viewModel.updateEntry(transaction.copy(date = it)) },
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
                    value = transaction.category.name,
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
                            text = { Text(selectionOption.name) },
                            onClick = {
                                viewModel.updateEntry(transaction.copy(category = selectionOption))
                                categoryExpanded = false
                            },
                        )
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
                    value = transaction.type.name,
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
                            text = {
                                Text(selectionOption.name)
                            },
                            onClick = {
                                viewModel.updateEntry(transaction.copy(type = selectionOption))
                                typeExpanded = false
                            },
                        )
                    }
                }
            }

        }

    }
}
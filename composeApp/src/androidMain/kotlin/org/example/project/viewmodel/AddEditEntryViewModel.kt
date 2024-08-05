package org.example.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.models.Entry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.project.domain.local.entity.AmountEntity
import org.example.project.domain.repo.AmountRepository

class EntryViewModel(private val repository: AmountRepository) : ViewModel() {
    private val _entries = MutableStateFlow<List<AmountEntity>>(emptyList())
    val entries: StateFlow<List<AmountEntity>> = _entries

    val id = mutableStateOf(0L)

    fun getEntryById() {
        viewModelScope.launch {
            repository.getEntryById(id.value)
        }
    }

}

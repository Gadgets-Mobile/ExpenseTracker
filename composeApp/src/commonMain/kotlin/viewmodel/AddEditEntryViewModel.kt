package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.models.Entry
import domain.repo.AmountRepository
import domain.repo.getAmountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddEditEntryViewModel(
    private val repository: AmountRepository = getAmountRepository(),
) : ViewModel() {

    private val _entry = MutableStateFlow(Entry())
    val entry = _entry.asStateFlow()

    fun getEntryById(id: Long) {
        viewModelScope.launch {
            repository.getEntryById(id)?.let {
                _entry.update { it }
            }
        }
    }

    fun deleteById(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun upsertData(entry: Entry) {
        viewModelScope.launch {
            if (entry.id == null) {
                val lastEntry = repository.getAllEntries().lastOrNull()
                val newId = lastEntry?.id?.plus(1) ?: 1
                repository.upsertData(entry.copy(id = newId))
            } else {
                repository.upsertData(entry)
            }
        }
    }

}

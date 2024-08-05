package viewmodel

import androidx.lifecycle.ViewModel
import data.models.Entry
import domain.repo.AmountRepository
import domain.repo.getAmountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel(
    private val repository: AmountRepository = getAmountRepository(),
) : ViewModel() {
    private val _entries = MutableStateFlow<List<Entry>>(emptyList())
    val entries = _entries.asStateFlow()

    fun getAllEntries() {
        _entries.update { repository.getAllEntries() }
    }
}

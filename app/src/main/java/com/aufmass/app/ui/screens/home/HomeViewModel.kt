package com.aufmass.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aufmass.app.data.local.entity.AufmassEntity
import com.aufmass.app.data.repository.AufmassRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: AufmassRepository
) : ViewModel() {

    val aufmassList: StateFlow<List<AufmassEntity>> = repository.getAllAufmass()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun createNewAufmass(titel: String, onSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.insert(AufmassEntity(titel = titel))
            onSuccess(id)
        }
    }

    fun deleteAufmass(aufmass: AufmassEntity) {
        viewModelScope.launch {
            repository.delete(aufmass)
        }
    }

    class Factory(private val repository: AufmassRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repository) as T
        }
    }
}

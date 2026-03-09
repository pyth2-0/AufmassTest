package com.aufmass.app.ui.screens.aufmass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aufmass.app.data.local.entity.*
import com.aufmass.app.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AufmassDetailUiState(
    val aufmass: AufmassEntity? = null,
    val raeume: List<RaumEntity> = emptyList(),
    val glasList: List<GlasEntity> = emptyList(),
    val bodenSeList: List<BodenSeEntity> = emptyList(),
    val isLoading: Boolean = true
)

class AufmassDetailViewModel(
    val aufmassId: Long,
    private val aufmassRepository: AufmassRepository,
    private val raumRepository: RaumRepository,
    private val glasRepository: GlasRepository,
    private val bodenSeRepository: BodenSeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AufmassDetailUiState())
    val uiState: StateFlow<AufmassDetailUiState> = _uiState

    init {
        loadAufmass()
        loadRaeume()
        loadGlas()
        loadBodenSe()
    }

    private fun loadAufmass() {
        viewModelScope.launch {
            val aufmass = aufmassRepository.getAufmassById(aufmassId)
            _uiState.update { it.copy(aufmass = aufmass, isLoading = false) }
        }
    }

    private fun loadRaeume() {
        viewModelScope.launch {
            raumRepository.getRaeumeByAufmassId(aufmassId).collect { list ->
                _uiState.update { it.copy(raeume = list) }
            }
        }
    }

    private fun loadGlas() {
        viewModelScope.launch {
            glasRepository.getGlasByAufmassId(aufmassId).collect { list ->
                _uiState.update { it.copy(glasList = list) }
            }
        }
    }

    private fun loadBodenSe() {
        viewModelScope.launch {
            bodenSeRepository.getBodenSeByAufmassId(aufmassId).collect { list ->
                _uiState.update { it.copy(bodenSeList = list) }
            }
        }
    }

    fun updateAufmass(aufmass: AufmassEntity) {
        viewModelScope.launch {
            aufmassRepository.update(aufmass)
            loadAufmass()
        }
    }

    fun deleteRaum(raum: RaumEntity) {
        viewModelScope.launch {
            raumRepository.delete(raum)
        }
    }

    fun deleteGlas(glas: GlasEntity) {
        viewModelScope.launch {
            glasRepository.delete(glas)
        }
    }

    fun deleteBodenSe(bodenSe: BodenSeEntity) {
        viewModelScope.launch {
            bodenSeRepository.delete(bodenSe)
        }
    }

    class Factory(
        private val aufmassId: Long,
        private val aufmassRepository: AufmassRepository,
        private val raumRepository: RaumRepository,
        private val glasRepository: GlasRepository,
        private val bodenSeRepository: BodenSeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AufmassDetailViewModel(
                aufmassId,
                aufmassRepository,
                raumRepository,
                glasRepository,
                bodenSeRepository
            ) as T
        }
    }
}

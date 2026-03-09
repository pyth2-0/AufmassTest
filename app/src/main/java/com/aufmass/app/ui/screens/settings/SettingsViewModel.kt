package com.aufmass.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aufmass.app.data.local.entity.*
import com.aufmass.app.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SettingsUiState(
    val raumarten: List<RaumartEntity> = emptyList(),
    val rhysmen: List<RhythmusEntity> = emptyList(),
    val bodenbelage: List<BodenbelagEntity> = emptyList(),
    val glasarten: List<GlasartEntity> = emptyList(),
    val lvEinstellungen: List<LvEinstellungEntity> = emptyList()
)

class SettingsViewModel(
    private val raumartRepository: RaumartRepository,
    private val rhythmusRepository: RhythmusRepository,
    private val bodenbelagRepository: BodenbelagRepository,
    private val glasartRepository: GlasartRepository,
    private val lvEinstellungRepository: LvEinstellungRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            raumartRepository.getAllRaumarten().collect { list ->
                _uiState.update { it.copy(raumarten = list) }
                if (list.isEmpty()) seedRaumarten()
            }
        }
        viewModelScope.launch {
            rhythmusRepository.getAllRhythmen().collect { list ->
                _uiState.update { it.copy(rhysmen = list) }
                if (list.isEmpty()) seedRhythmen()
            }
        }
        viewModelScope.launch {
            bodenbelagRepository.getAllBodenbelage().collect { list ->
                _uiState.update { it.copy(bodenbelage = list) }
                if (list.isEmpty()) seedBodenbelage()
            }
        }
        viewModelScope.launch {
            glasartRepository.getAllGlasarten().collect { list ->
                _uiState.update { it.copy(glasarten = list) }
                if (list.isEmpty()) seedGlasarten()
            }
        }
        viewModelScope.launch {
            lvEinstellungRepository.getAllLvEinstellungen().collect { list ->
                _uiState.update { it.copy(lvEinstellungen = list) }
            }
        }
    }

    private suspend fun seedRaumarten() {
        val defaults = listOf(
            "Büro" to 200.0, "Besprechungsraum" to 210.0, "WC" to 60.0,
            "Waschraum" to 90.0, "Duschraum" to 90.0, "Teeküchen" to 120.0,
            "Aufenthaltsräume" to 120.0, "Umkleiden" to 160.0, "Aufzüge" to 120.0,
            "Flur" to 210.0, "Eingang" to 130.0, "Windfang" to 130.0,
            "Foyers" to 130.0, "TH" to 130.0, "Nebenraum" to 200.0,
            "Behandlungsraum" to 120.0, "Lager" to 200.0, "Klassenraum" to 250.0,
            "Sportraum" to 250.0, "Gruppenraum" to 120.0
        )
        defaults.forEach { (name, schnitt) ->
            raumartRepository.insert(RaumartEntity(bezeichnung = name, schnittvorgabe = schnitt))
        }
    }

    private suspend fun seedBodenbelage() {
        val defaults = listOf(
            "Betonwerkstein" to "BTW", "Designboden mit Struktur" to "DBS",
            "Designboden ohne Struktur" to "DBOS", "Elastomerbelag" to "Noppe",
            "Fliese" to "FL", "Holzdiele" to "Diele", "Kugelg./FL" to "KG/FL",
            "Linoleum" to "Lino", "Nadelflies" to "NF", "Parkett" to "Park.",
            "PVC" to "PVC", "sonstiges" to "sonst", "Estrich" to "Estrich",
            "Beton" to "Beton", "Stein" to "Stein", "Teppich" to "Tepp"
        )
        defaults.forEach { (name, abk) ->
            bodenbelagRepository.insert(BodenbelagEntity(bezeichnung = name, abkuerzung = abk))
        }
    }

    private suspend fun seedRhythmen() {
        val defaults = listOf(
            "7/W" to 7.0, "6/W" to 6.0, "5/W" to 5.0, "4/W" to 4.0,
            "3/W" to 3.0, "2/3/W" to 2.5, "2/W" to 2.0, "1/W" to 1.0,
            "2/M" to 0.5, "1/M" to 0.25
        )
        defaults.forEach { (text, wert) ->
            rhythmusRepository.insert(RhythmusEntity(klartext = text, exportwert = wert))
        }
    }

    private suspend fun seedGlasarten() {
        val defaults = listOf("Dreh-Kipp", "Fest", "Schaufenster", "Schiebefenster", "Tür")
        defaults.forEach { name ->
            glasartRepository.insert(GlasartEntity(bezeichnung = name))
        }
    }

    fun addRaumart(bezeichnung: String, schnittvorgabe: Double) {
        viewModelScope.launch {
            raumartRepository.insert(RaumartEntity(bezeichnung = bezeichnung, schnittvorgabe = schnittvorgabe))
        }
    }

    fun updateRaumart(raumart: RaumartEntity) {
        viewModelScope.launch {
            raumartRepository.update(raumart)
        }
    }

    fun deleteRaumart(raumart: RaumartEntity) {
        viewModelScope.launch {
            raumartRepository.delete(raumart)
        }
    }

    fun addRhythmus(klartext: String, exportwert: Double, lvWert: Double = 1.0) {
        viewModelScope.launch {
            rhythmusRepository.insert(RhythmusEntity(klartext = klartext, exportwert = exportwert, lvWert = lvWert))
        }
    }

    fun updateRhythmus(rhythmus: RhythmusEntity) {
        viewModelScope.launch {
            rhythmusRepository.update(rhythmus)
        }
    }

    fun deleteRhythmus(rhythmus: RhythmusEntity) {
        viewModelScope.launch {
            rhythmusRepository.delete(rhythmus)
        }
    }

    fun addBodenbelag(bezeichnung: String, abkuerzung: String) {
        viewModelScope.launch {
            bodenbelagRepository.insert(BodenbelagEntity(bezeichnung = bezeichnung, abkuerzung = abkuerzung))
        }
    }

    fun updateBodenbelag(bodenbelag: BodenbelagEntity) {
        viewModelScope.launch {
            bodenbelagRepository.update(bodenbelag)
        }
    }

    fun deleteBodenbelag(bodenbelag: BodenbelagEntity) {
        viewModelScope.launch {
            bodenbelagRepository.delete(bodenbelag)
        }
    }

    fun addGlasart(bezeichnung: String) {
        viewModelScope.launch {
            glasartRepository.insert(GlasartEntity(bezeichnung = bezeichnung))
        }
    }

    fun updateGlasart(glasart: GlasartEntity) {
        viewModelScope.launch {
            glasartRepository.update(glasart)
        }
    }

    fun deleteGlasart(glasart: GlasartEntity) {
        viewModelScope.launch {
            glasartRepository.delete(glasart)
        }
    }

    fun addLvEinstellung(raumart: String, spalte: String, aufgabe: String, platzhalter: String) {
        viewModelScope.launch {
            lvEinstellungRepository.insert(
                LvEinstellungEntity(
                    raumart = raumart,
                    spalte = spalte,
                    aufgabe = aufgabe,
                    rhythmusPlatzhalter = platzhalter
                )
            )
        }
    }

    fun updateLvEinstellung(lv: LvEinstellungEntity) {
        viewModelScope.launch {
            lvEinstellungRepository.update(lv)
        }
    }

    fun deleteLvEinstellung(lv: LvEinstellungEntity) {
        viewModelScope.launch {
            lvEinstellungRepository.delete(lv)
        }
    }

    class Factory(
        private val raumartRepository: RaumartRepository,
        private val rhythmusRepository: RhythmusRepository,
        private val bodenbelagRepository: BodenbelagRepository,
        private val glasartRepository: GlasartRepository,
        private val lvEinstellungRepository: LvEinstellungRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(
                raumartRepository,
                rhythmusRepository,
                bodenbelagRepository,
                glasartRepository,
                lvEinstellungRepository
            ) as T
        }
    }
}

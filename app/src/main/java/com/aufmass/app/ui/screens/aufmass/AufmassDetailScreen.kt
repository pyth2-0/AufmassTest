package com.aufmass.app.ui.screens.aufmass

import android.text.InputType
import android.widget.EditText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aufmass.app.data.local.entity.*
import com.aufmass.app.data.repository.*
import com.aufmass.app.ui.components.StylusEditText
import com.aufmass.app.util.NumberFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AufmassDetailScreen(
    viewModel: AufmassDetailViewModel,
    raumRepository: RaumRepository,
    raumartRepository: RaumartRepository,
    bodenbelagRepository: BodenbelagRepository,
    rhythmusRepository: RhythmusRepository,
    glasRepository: GlasRepository,
    glasartRepository: GlasartRepository,
    bodenSeRepository: BodenSeRepository,
    objektfragebogenRepository: ObjektfragebogenRepository,
    lvEinstellungRepository: LvEinstellungRepository,
    onNavigateBack: () -> Unit,
    onExport: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var pendingTabIndex by remember { mutableIntStateOf(0) }
    var pendingTabType by remember { mutableStateOf("") }
    var hasObjektChanges by remember { mutableStateOf(false) }
    var hasStammdatenChanges by remember { mutableStateOf(false) }
    var originalStammdaten by remember { mutableStateOf(uiState.aufmass) }
    val tabs = listOf("Stammdaten", "Objekt", "Räume", "LV", "Glas", "Boden/S&E")

    LaunchedEffect(uiState.aufmass) {
        if (uiState.aufmass != null) {
            originalStammdaten = uiState.aufmass
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.aufmass?.titel ?: "Aufmaß") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                },
                actions = {
                    IconButton(onClick = onExport) {
                        Icon(Icons.Default.Share, contentDescription = "Exportieren")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            if ((hasStammdatenChanges && selectedTab == 0) || (hasObjektChanges && selectedTab == 1)) {
                                pendingTabIndex = index
                                pendingTabType = if (selectedTab == 0) "stammdaten" else "objekt"
                                showSaveDialog = true
                            } else {
                                selectedTab = index
                            }
                        },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> {
                    var firma by remember(uiState.aufmass) { mutableStateOf(uiState.aufmass?.firma ?: "") }
                    var anschrift by remember(uiState.aufmass) { mutableStateOf(uiState.aufmass?.anschrift ?: "") }
                    var objektanschrift by remember(uiState.aufmass) { mutableStateOf(uiState.aufmass?.objektanschrift ?: "") }
                    var standardrhythmus by remember(uiState.aufmass) { mutableStateOf(uiState.aufmass?.standardrhythmus ?: "1/Woche") }
                    var wochentage by remember(uiState.aufmass) { mutableStateOf(uiState.aufmass?.wochentage ?: "") }
                    var reinigungszeiten by remember(uiState.aufmass) { mutableStateOf(uiState.aufmass?.reinigungszeiten ?: "") }
                    var reinigungstage by remember(uiState.aufmass) { mutableStateOf(uiState.aufmass?.reinigungstage ?: "") }
                    var notizen by remember(uiState.aufmass) { mutableStateOf(uiState.aufmass?.notizen ?: "") }
                    var showRhythmusDropdown by remember { mutableStateOf(false) }
                    val rhysmen by rhythmusRepository.getAllRhythmen().collectAsState(initial = emptyList())

                    hasStammdatenChanges = originalStammdaten?.let { original ->
                        firma != original.firma ||
                        anschrift != original.anschrift ||
                        objektanschrift != original.objektanschrift ||
                        standardrhythmus != original.standardrhythmus ||
                        wochentage != original.wochentage ||
                        reinigungszeiten != original.reinigungszeiten ||
                        reinigungstage != original.reinigungstage ||
                        notizen != original.notizen
                    } ?: false

                    val editTextFirma = remember { mutableStateOf<EditText?>(null) }
                    val editTextAnschrift = remember { mutableStateOf<EditText?>(null) }
                    val editTextObjektanschrift = remember { mutableStateOf<EditText?>(null) }
                    val editTextReinigungszeiten = remember { mutableStateOf<EditText?>(null) }
                    val editTextNotizen = remember { mutableStateOf<EditText?>(null) }

                    val selectedDays = remember(reinigungstage) {
                        reinigungstage.split(",").filter { it.isNotBlank() }.toSet()
                    }

                    fun toggleDay(day: String) {
                        val current = selectedDays.toMutableSet()
                        if (current.contains(day)) current.remove(day) else current.add(day)
                        reinigungstage = current.sortedBy { listOf("Mo", "Di", "Mi", "Do", "Fr", "Sa", "So").indexOf(it) }.joinToString(",")
                    }

                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Übersicht", style = MaterialTheme.typography.labelLarge)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("${uiState.raeume.size}", style = MaterialTheme.typography.headlineMedium)
                                        Text("Räume", style = MaterialTheme.typography.labelSmall)
                                        Text("${NumberFormatter.formatFlaeche(uiState.raeume.sumOf { it.gesamtflaeche })}", style = MaterialTheme.typography.labelSmall)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("${uiState.glasList.size}", style = MaterialTheme.typography.headlineMedium)
                                        Text("Glas", style = MaterialTheme.typography.labelSmall)
                                        Text("${NumberFormatter.formatFlaeche(uiState.glasList.sumOf { it.flaeche })}", style = MaterialTheme.typography.labelSmall)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("${uiState.bodenSeList.size}", style = MaterialTheme.typography.headlineMedium)
                                        Text("Boden/S&E", style = MaterialTheme.typography.labelSmall)
                                        Text("${NumberFormatter.formatFlaeche(uiState.bodenSeList.sumOf { it.flaeche })}", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }

                        StylusEditText(
                            value = firma,
                            onValueChange = { firma = it },
                            label = "Firma / Name",
                            modifier = Modifier.fillMaxWidth(),
                            imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                            onImeNext = { editTextAnschrift.value?.apply { post { requestFocus() } } },
                            onViewCreated = { editTextFirma.value = it }
                        )
                        StylusEditText(
                            value = anschrift,
                            onValueChange = { anschrift = it },
                            label = "Anschrift",
                            modifier = Modifier.fillMaxWidth(),
                            imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                            onImeNext = { editTextObjektanschrift.value?.apply { post { requestFocus() } } },
                            onViewCreated = { editTextAnschrift.value = it }
                        )
                        StylusEditText(
                            value = objektanschrift,
                            onValueChange = { objektanschrift = it },
                            label = "Objektanschrift",
                            modifier = Modifier.fillMaxWidth(),
                            imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                            onImeNext = { editTextReinigungszeiten.value?.apply { post { requestFocus() } } },
                            onViewCreated = { editTextObjektanschrift.value = it }
                        )
                        ExposedDropdownMenuBox(
                            expanded = showRhythmusDropdown,
                            onExpandedChange = { showRhythmusDropdown = it }
                        ) {
                            OutlinedTextField(
                                value = standardrhythmus,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Standardrhythmus") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showRhythmusDropdown) },
                                modifier = Modifier.fillMaxWidth().menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = showRhythmusDropdown,
                                onDismissRequest = { showRhythmusDropdown = false }
                            ) {
                                rhysmen.forEach { rhythmus ->
                                    DropdownMenuItem(
                                        text = { Text(rhythmus.klartext) },
                                        onClick = {
                                            standardrhythmus = rhythmus.klartext
                                            showRhythmusDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                        Text("Reinigungstage:", style = MaterialTheme.typography.labelMedium)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf("Mo", "Di", "Mi", "Do", "Fr", "Sa", "So").forEach { day ->
                                FilterChip(
                                    selected = selectedDays.contains(day),
                                    onClick = { toggleDay(day) },
                                    label = { Text(day) }
                                )
                            }
                        }
                        StylusEditText(
                            value = reinigungszeiten,
                            onValueChange = { reinigungszeiten = it },
                            label = "Reinigungszeiten (z.B. 06:00 - 14:00)",
                            modifier = Modifier.fillMaxWidth(),
                            imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                            onImeNext = { editTextNotizen.value?.apply { post { requestFocus() } } },
                            onViewCreated = { editTextReinigungszeiten.value = it }
                        )
                        StylusEditText(
                            value = notizen,
                            onValueChange = { notizen = it },
                            label = "Notizen",
                            modifier = Modifier.fillMaxWidth(),
                            inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE,
                            imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_DONE,
                            onImeDone = { },
                            onViewCreated = { editTextNotizen.value = it }
                        )
                        Button(
                            onClick = {
                                uiState.aufmass?.let { aufmass ->
                                    val updated = aufmass.copy(
                                        firma = firma,
                                        anschrift = anschrift,
                                        objektanschrift = objektanschrift,
                                        standardrhythmus = standardrhythmus,
                                        wochentage = wochentage,
                                        reinigungszeiten = reinigungszeiten,
                                        reinigungstage = reinigungstage,
                                        notizen = notizen
                                    )
                                    viewModel.updateAufmass(updated)
                                    originalStammdaten = updated
                                    hasStammdatenChanges = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Stammdaten speichern")
                        }
                    }
                }
                1 -> ObjektfragebogenScreen(
                    aufmassId = viewModel.aufmassId,
                    repository = objektfragebogenRepository,
                    onHasChangesChanged = { hasChanges -> hasObjektChanges = hasChanges }
                )
                2 -> RaeumeScreen(
                    aufmassId = viewModel.aufmassId,
                    aufmass = uiState.aufmass,
                    raumRepository = raumRepository,
                    raumartRepository = raumartRepository,
                    bodenbelagRepository = bodenbelagRepository,
                    rhythmusRepository = rhythmusRepository
                )
                3 -> {
                    val raumarten by raumartRepository.getAllRaumarten().collectAsState(initial = emptyList())
                    LvScreen(
                        raeume = uiState.raeume,
                        lvRepository = lvEinstellungRepository,
                        rhythmusRepository = rhythmusRepository,
                        raumarten = raumarten
                    )
                }
                4 -> GlasScreen(
                    aufmassId = viewModel.aufmassId,
                    aufmassTitel = uiState.aufmass?.titel ?: "",
                    glasRepository = glasRepository,
                    glasartRepository = glasartRepository
                )
                5 -> BodenSeScreen(
                    aufmassId = viewModel.aufmassId,
                    aufmassTitel = uiState.aufmass?.titel ?: "",
                    bodenSeRepository = bodenSeRepository
                )
            }
        }

        if (showSaveDialog) {
            val changeTypeText = when (pendingTabType) {
                "stammdaten" -> "in den Stammdaten"
                "objekt" -> "im Objektfragebogen"
                else -> ""
            }
            AlertDialog(
                onDismissRequest = { showSaveDialog = false },
                title = { Text("Ungespeicherte Änderungen") },
                text = { Text("Sie haben ungespeicherte Änderungen $changeTypeText.") },
                confirmButton = {
                    TextButton(onClick = {
                        when (pendingTabType) {
                            "stammdaten" -> {
                                uiState.aufmass?.let { aufmass ->
                                    viewModel.updateAufmass(aufmass)
                                    originalStammdaten = aufmass
                                    hasStammdatenChanges = false
                                }
                            }
                            "objekt" -> {
                                hasObjektChanges = false
                            }
                        }
                        selectedTab = pendingTabIndex
                        showSaveDialog = false
                    }) {
                        Text("Speichern & Wechseln")
                    }
                },
                dismissButton = {
                    Row {
                        TextButton(onClick = {
                            hasObjektChanges = false
                            selectedTab = pendingTabIndex
                            showSaveDialog = false
                        }) {
                            Text("Verwerfen")
                        }
                        TextButton(onClick = { showSaveDialog = false }) {
                            Text("Abbrechen")
                        }
                    }
                }
            )
        }
    }
}

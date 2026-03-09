package com.aufmass.app.ui.screens.settings

import android.content.Context
import android.provider.UserDictionary
import android.text.InputType
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aufmass.app.data.local.entity.*
import com.aufmass.app.ui.components.StylusEditText
import com.aufmass.app.util.NumberFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Raumarten", "Rhythmen", "Bodenbeläge", "Glasarten", "LV")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Einstellungen") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            ScrollableTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title) })
                }
            }

            when (selectedTab) {
                0 -> RaumartenEditableTab(uiState, viewModel)
                1 -> RhythmenEditableTab(uiState, viewModel)
                2 -> BodenbelageEditableTab(uiState, viewModel)
                3 -> GlasartenEditableTab(uiState, viewModel)
                4 -> LvEditableTab(uiState, viewModel)
            }
        }
    }
}

@Composable
fun RaumartenEditableTab(uiState: SettingsUiState, viewModel: SettingsViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingRaumart by remember { mutableStateOf<RaumartEntity?>(null) }
    var bezeichnung by remember { mutableStateOf("") }
    var schnittvorgabe by remember { mutableStateOf("") }
    val context = LocalContext.current
    var expandedCards by remember { mutableStateOf(setOf<Long>()) }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(uiState.raumarten) { raumart ->
                val isExpanded = expandedCards.contains(raumart.id)
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedCards = if (isExpanded) {
                                        expandedCards - raumart.id
                                    } else {
                                        expandedCards + raumart.id
                                    }
                                }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(raumart.bezeichnung, style = MaterialTheme.typography.titleMedium)
                                Text("${NumberFormatter.formatDecimal(raumart.schnittvorgabe)} m²/h", style = MaterialTheme.typography.bodySmall)
                            }
                            Icon(
                                if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (isExpanded) "Einklappen" else "Ausklappen"
                            )
                        }
                        if (isExpanded) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = { addToDictionary(context, raumart.bezeichnung) }) {
                                    Icon(Icons.Default.TextFields, "Zum Wörterbuch hinzufügen")
                                }
                                IconButton(onClick = {
                                    editingRaumart = raumart
                                    bezeichnung = raumart.bezeichnung
                                    schnittvorgabe = NumberFormatter.formatDecimal(raumart.schnittvorgabe)
                                    showEditDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, "Bearbeiten")
                                }
                                IconButton(onClick = { viewModel.deleteRaumart(raumart) }) {
                                    Icon(Icons.Default.Delete, "Löschen", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(onClick = { showDialog = true }, modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.Add, "Hinzufügen")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Raumart hinzufügen") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Bezeichnung:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = bezeichnung, onValueChange = { bezeichnung = it }, label = "Bezeichnung", modifier = Modifier.fillMaxWidth())
                    Text("Schnittvorgabe (m²/h):", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = schnittvorgabe, onValueChange = { schnittvorgabe = it }, label = "m²/h", modifier = Modifier.fillMaxWidth(), inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL, allowComma = true)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val schnitt = NumberFormatter.parseDecimal(schnittvorgabe) ?: 0.0
                    if (bezeichnung.isNotBlank()) {
                        viewModel.addRaumart(bezeichnung, schnitt)
                        showDialog = false
                        bezeichnung = ""
                        schnittvorgabe = ""
                    }
                }) { Text("Speichern") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Abbrechen") }
            }
        )
    }

    if (showEditDialog && editingRaumart != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Raumart bearbeiten") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Bezeichnung:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = bezeichnung, onValueChange = { bezeichnung = it }, label = "Bezeichnung", modifier = Modifier.fillMaxWidth())
                    Text("Schnittvorgabe (m²/h):", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = schnittvorgabe, onValueChange = { schnittvorgabe = it }, label = "m²/h", modifier = Modifier.fillMaxWidth(), inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL, allowComma = true)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val schnitt = NumberFormatter.parseDecimal(schnittvorgabe) ?: 0.0
                    if (bezeichnung.isNotBlank()) {
                        viewModel.updateRaumart(editingRaumart!!.copy(bezeichnung = bezeichnung, schnittvorgabe = schnitt))
                        showEditDialog = false
                        editingRaumart = null
                        bezeichnung = ""
                        schnittvorgabe = ""
                    }
                }) { Text("Speichern") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Abbrechen") }
            }
        )
    }
}

@Composable
fun RhythmenEditableTab(uiState: SettingsUiState, viewModel: SettingsViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingRhythmus by remember { mutableStateOf<RhythmusEntity?>(null) }
    var klartext by remember { mutableStateOf("") }
    var exportwert by remember { mutableStateOf("") }
    var lvWert by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(uiState.rhysmen) { rhythmus ->
                ListItem(
                    headlineContent = { Text("${rhythmus.klartext} (Aufmaß: ${NumberFormatter.formatDecimal(rhythmus.exportwert)}, LV: ${NumberFormatter.formatDecimal(rhythmus.lvWert)})") },
                    trailingContent = {
                        Row {
                            IconButton(onClick = {
                                editingRhythmus = rhythmus
                                klartext = rhythmus.klartext
                                exportwert = NumberFormatter.formatDecimal(rhythmus.exportwert)
                                lvWert = NumberFormatter.formatDecimal(rhythmus.lvWert)
                                showEditDialog = true
                            }) {
                                Icon(Icons.Default.Edit, "Bearbeiten")
                            }
                            IconButton(onClick = { viewModel.deleteRhythmus(rhythmus) }) {
                                Icon(Icons.Default.Delete, "Löschen", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                )
            }
        }
        FloatingActionButton(onClick = { showDialog = true }, modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.Add, "Hinzufügen")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Rhythmus hinzufügen") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Klartext (z.B. 1/Woche):", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = klartext, onValueChange = { klartext = it }, label = "z.B. 1/Woche", modifier = Modifier.fillMaxWidth())
                    Text("Rhythmus Aufmaß (z.B. 4,33):", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = exportwert, onValueChange = { exportwert = it }, label = "z.B. 4,33", modifier = Modifier.fillMaxWidth(), inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL, allowComma = true)
                    Text("Rhythmus LV (z.B. 1):", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = lvWert, onValueChange = { lvWert = it }, label = "z.B. 1", modifier = Modifier.fillMaxWidth(), inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL, allowComma = true)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val wert = NumberFormatter.parseDecimal(exportwert) ?: 0.0
                    val lv = NumberFormatter.parseDecimal(lvWert) ?: 1.0
                    if (klartext.isNotBlank()) {
                        viewModel.addRhythmus(klartext, wert, lv)
                        showDialog = false
                        klartext = ""
                        exportwert = ""
                        lvWert = ""
                    }
                }) { Text("Speichern") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Abbrechen") }
            }
        )
    }

    if (showEditDialog && editingRhythmus != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Rhythmus bearbeiten") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Klartext:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = klartext, onValueChange = { klartext = it }, label = "z.B. 1/Woche", modifier = Modifier.fillMaxWidth())
                    Text("Rhythmus Aufmaß:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = exportwert, onValueChange = { exportwert = it }, label = "z.B. 4,33", modifier = Modifier.fillMaxWidth(), inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL, allowComma = true)
                    Text("Rhythmus LV:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = lvWert, onValueChange = { lvWert = it }, label = "z.B. 1", modifier = Modifier.fillMaxWidth(), inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL, allowComma = true)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val wert = NumberFormatter.parseDecimal(exportwert) ?: 0.0
                    val lv = NumberFormatter.parseDecimal(lvWert) ?: 1.0
                    if (klartext.isNotBlank()) {
                        viewModel.updateRhythmus(editingRhythmus!!.copy(klartext = klartext, exportwert = wert, lvWert = lv))
                        showEditDialog = false
                        editingRhythmus = null
                        klartext = ""
                        exportwert = ""
                        lvWert = ""
                    }
                }) { Text("Speichern") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Abbrechen") }
            }
        )
    }
}

@Composable
fun BodenbelageEditableTab(uiState: SettingsUiState, viewModel: SettingsViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingBodenbelag by remember { mutableStateOf<BodenbelagEntity?>(null) }
    var bezeichnung by remember { mutableStateOf("") }
    var abkuerzung by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(uiState.bodenbelage) { bodenbelag ->
                ListItem(
                    headlineContent = { Text("${bodenbelag.bezeichnung} (${bodenbelag.abkuerzung})") },
                    trailingContent = {
                        Row {
                            IconButton(onClick = {
                                editingBodenbelag = bodenbelag
                                bezeichnung = bodenbelag.bezeichnung
                                abkuerzung = bodenbelag.abkuerzung
                                showEditDialog = true
                            }) {
                                Icon(Icons.Default.Edit, "Bearbeiten")
                            }
                            IconButton(onClick = { viewModel.deleteBodenbelag(bodenbelag) }) {
                                Icon(Icons.Default.Delete, "Löschen", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                )
            }
        }
        FloatingActionButton(onClick = { showDialog = true }, modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.Add, "Hinzufügen")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Bodenbelag hinzufügen") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Bezeichnung:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = bezeichnung, onValueChange = { bezeichnung = it }, label = "z.B. PVC", modifier = Modifier.fillMaxWidth())
                    Text("Abkürzung:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = abkuerzung, onValueChange = { abkuerzung = it }, label = "z.B. PVC", modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (bezeichnung.isNotBlank() && abkuerzung.isNotBlank()) {
                        viewModel.addBodenbelag(bezeichnung, abkuerzung)
                        showDialog = false
                        bezeichnung = ""
                        abkuerzung = ""
                    }
                }) { Text("Speichern") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Abbrechen") }
            }
        )
    }

    if (showEditDialog && editingBodenbelag != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Bodenbelag bearbeiten") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Bezeichnung:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = bezeichnung, onValueChange = { bezeichnung = it }, label = bezeichnung.ifEmpty { "z.B. PVC" }, modifier = Modifier.fillMaxWidth())
                    Text("Abkürzung:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = abkuerzung, onValueChange = { abkuerzung = it }, label = abkuerzung.ifEmpty { "z.B. PVC" }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (bezeichnung.isNotBlank() && abkuerzung.isNotBlank()) {
                        viewModel.updateBodenbelag(editingBodenbelag!!.copy(bezeichnung = bezeichnung, abkuerzung = abkuerzung))
                        showEditDialog = false
                        editingBodenbelag = null
                        bezeichnung = ""
                        abkuerzung = ""
                    }
                }) { Text("Speichern") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Abbrechen") }
            }
        )
    }
}

@Composable
fun GlasartenEditableTab(uiState: SettingsUiState, viewModel: SettingsViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingGlasart by remember { mutableStateOf<GlasartEntity?>(null) }
    var bezeichnung by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(uiState.glasarten) { glasart ->
                ListItem(
                    headlineContent = { Text(glasart.bezeichnung) },
                    trailingContent = {
                        Row {
                            IconButton(onClick = {
                                editingGlasart = glasart
                                bezeichnung = glasart.bezeichnung
                                showEditDialog = true
                            }) {
                                Icon(Icons.Default.Edit, "Bearbeiten")
                            }
                            IconButton(onClick = { viewModel.deleteGlasart(glasart) }) {
                                Icon(Icons.Default.Delete, "Löschen", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                )
            }
        }
        FloatingActionButton(onClick = { showDialog = true }, modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.Add, "Hinzufügen")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Glasart hinzufügen") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Bezeichnung:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = bezeichnung, onValueChange = { bezeichnung = it }, label = "z.B. Dreh-Kipp", modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (bezeichnung.isNotBlank()) {
                        viewModel.addGlasart(bezeichnung)
                        showDialog = false
                        bezeichnung = ""
                    }
                }) { Text("Speichern") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Abbrechen") }
            }
        )
    }

    if (showEditDialog && editingGlasart != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Glasart bearbeiten") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Bezeichnung:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = bezeichnung, onValueChange = { bezeichnung = it }, label = bezeichnung.ifEmpty { "z.B. Dreh-Kipp" }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (bezeichnung.isNotBlank()) {
                        viewModel.updateGlasart(editingGlasart!!.copy(bezeichnung = bezeichnung))
                        showEditDialog = false
                        editingGlasart = null
                        bezeichnung = ""
                    }
                }) { Text("Speichern") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Abbrechen") }
            }
        )
    }
}

@Composable
fun LvEditableTab(uiState: SettingsUiState, viewModel: SettingsViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingLv by remember { mutableStateOf<LvEinstellungEntity?>(null) }
    var raumart by remember { mutableStateOf("") }
    var spalte by remember { mutableStateOf("") }
    var aufgabe by remember { mutableStateOf("") }
    var platzhalter by remember { mutableStateOf("{Rhythmus}") }
    
    // Gruppiere LV-Einstellungen nach Raumart
    val lvByRaumart = uiState.lvEinstellungen.groupBy { it.raumart }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            lvByRaumart.forEach { (raumartName, lvList) ->
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(raumartName, style = MaterialTheme.typography.titleMedium)
                            IconButton(onClick = {
                                // Vorbelegen mit dieser Raumart
                                raumart = raumartName
                                spalte = ""
                                aufgabe = ""
                                platzhalter = "{Rhythmus}"
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Add, "Aufgabe hinzufügen")
                            }
                        }
                    }
                }
                
                lvList.sortedBy { it.spalte }.forEach { lv ->
                    item {
                        ListItem(
                            headlineContent = { Text("${lv.spalte}: ${lv.aufgabe}") },
                            supportingContent = { Text("Platzhalter: ${lv.rhythmusPlatzhalter}") },
                            trailingContent = {
                                Row {
                                    IconButton(onClick = {
                                        editingLv = lv
                                        raumart = lv.raumart
                                        spalte = lv.spalte
                                        aufgabe = lv.aufgabe
                                        platzhalter = lv.rhythmusPlatzhalter
                                        showEditDialog = true
                                    }) {
                                        Icon(Icons.Default.Edit, "Bearbeiten")
                                    }
                                    IconButton(onClick = { viewModel.deleteLvEinstellung(lv) }) {
                                        Icon(Icons.Default.Delete, "Löschen", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
        FloatingActionButton(onClick = { 
            // Dialog vorbelegen
            raumart = ""
            spalte = ""
            aufgabe = ""
            platzhalter = "{Rhythmus}"
            showDialog = true 
        }, modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.Add, "Neue LV-Zeile hinzufügen")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("LV-Einstellung hinzufügen") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Raumart (z.B. Büro, WC, * für alle):", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = raumart, onValueChange = { raumart = it }, label = "z.B. Büro", modifier = Modifier.fillMaxWidth())
                    Text("Spalte:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = spalte, onValueChange = { spalte = it }, label = "z.B. A", modifier = Modifier.fillMaxWidth())
                    Text("Aufgabe:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = aufgabe, onValueChange = { aufgabe = it }, label = "z.B. Kehren", modifier = Modifier.fillMaxWidth())
                    Text("Rhythmus-Platzhalter:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = platzhalter, onValueChange = { platzhalter = it }, label = "{Rhythmus}", modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (raumart.isNotBlank() && spalte.isNotBlank() && aufgabe.isNotBlank()) {
                        viewModel.addLvEinstellung(raumart, spalte, aufgabe, platzhalter)
                        showDialog = false
                        raumart = ""
                        spalte = ""
                        aufgabe = ""
                        platzhalter = "{Rhythmus}"
                    }
                }) { Text("Speichern") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Abbrechen") }
            }
        )
    }

    if (showEditDialog && editingLv != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("LV-Einstellung bearbeiten") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Raumart:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = raumart, onValueChange = { raumart = it }, label = raumart.ifEmpty { "z.B. Büro" }, modifier = Modifier.fillMaxWidth())
                    Text("Spalte:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = spalte, onValueChange = { spalte = it }, label = spalte.ifEmpty { "z.B. A" }, modifier = Modifier.fillMaxWidth())
                    Text("Aufgabe:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = aufgabe, onValueChange = { aufgabe = it }, label = aufgabe.ifEmpty { "z.B. Kehren" }, modifier = Modifier.fillMaxWidth())
                    Text("Rhythmus-Platzhalter:", style = MaterialTheme.typography.labelMedium)
                    StylusEditText(value = platzhalter, onValueChange = { platzhalter = it }, label = platzhalter.ifEmpty { "{Rhythmus}" }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (raumart.isNotBlank() && spalte.isNotBlank() && aufgabe.isNotBlank()) {
                        viewModel.updateLvEinstellung(editingLv!!.copy(raumart = raumart, spalte = spalte, aufgabe = aufgabe, rhythmusPlatzhalter = platzhalter))
                        showEditDialog = false
                        editingLv = null
                        raumart = ""
                        spalte = ""
                        aufgabe = ""
                        platzhalter = "{Rhythmus}"
                    }
                }) { Text("Speichern") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Abbrechen") }
            }
        )
    }
}

private fun addToDictionary(context: Context, word: String) {
    try {
        @Suppress("DEPRECATION")
        UserDictionary.Words.addWord(
            context,
            word,
            1,
            null,
            java.util.Locale.getDefault()
        )
        Toast.makeText(context, "Wort zum Wörterbuch hinzugefügt: $word", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Wort zum Wörterbuch hinzugefügt: $word", Toast.LENGTH_SHORT).show()
    }
}

package com.aufmass.app.ui.screens.aufmass

import android.text.InputType
import android.widget.EditText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aufmass.app.data.local.entity.ObjektfragebogenEntity
import com.aufmass.app.data.repository.ObjektfragebogenRepository
import com.aufmass.app.ui.components.StylusEditText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjektfragebogenScreen(
    aufmassId: Long,
    repository: ObjektfragebogenRepository,
    onHasChangesChanged: ((Boolean) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var objektfragebogen by remember { mutableStateOf<ObjektfragebogenEntity?>(null) }
    var originalFragebogen by remember { mutableStateOf<ObjektfragebogenEntity?>(null) }
    
    var materialkammer by remember { mutableStateOf("") }
    var waschmaschine by remember { mutableStateOf(false) }
    var schmutzfangzone by remember { mutableStateOf(false) }
    var wasser by remember { mutableStateOf(false) }
    var strom by remember { mutableStateOf(false) }
    var muelltrennung by remember { mutableStateOf(false) }
    var muellentsorgung by remember { mutableStateOf("") }
    var aufzug by remember { mutableStateOf(false) }
    var reinigungszustand by remember { mutableStateOf("") }
    var wechselgruende by remember { mutableStateOf("") }
    var schluesselobjekt by remember { mutableStateOf(false) }
    var alarmanlage by remember { mutableStateOf(false) }
    var besonderheiten by remember { mutableStateOf("") }

    val editTextMuellentsorgung = remember { mutableStateOf<EditText?>(null) }
    val editTextReinigungszustand = remember { mutableStateOf<EditText?>(null) }
    val editTextWechselgruende = remember { mutableStateOf<EditText?>(null) }
    val editTextBesonderheiten = remember { mutableStateOf<EditText?>(null) }

    LaunchedEffect(aufmassId) {
        repository.getByAufmassId(aufmassId).collect { fragebogen ->
            if (fragebogen != null) {
                objektfragebogen = fragebogen
                originalFragebogen = fragebogen
                materialkammer = fragebogen.materialkammer
                waschmaschine = fragebogen.waschmaschine
                schmutzfangzone = fragebogen.schmutzfangzone
                wasser = fragebogen.wasser
                strom = fragebogen.strom
                muelltrennung = fragebogen.muelltrennung
                muellentsorgung = fragebogen.muellentsorgung
                aufzug = fragebogen.aufzug
                reinigungszustand = fragebogen.reinigungszustand
                wechselgruende = fragebogen.wechselgruende
                schluesselobjekt = fragebogen.schluesselobjekt
                alarmanlage = fragebogen.alarmanlage
                besonderheiten = fragebogen.besonderheiten
                onHasChangesChanged?.invoke(false)
            }
        }
    }

    val hasChanges = originalFragebogen?.let { original ->
        materialkammer != original.materialkammer ||
        waschmaschine != original.waschmaschine ||
        schmutzfangzone != original.schmutzfangzone ||
        wasser != original.wasser ||
        strom != original.strom ||
        muelltrennung != original.muelltrennung ||
        muellentsorgung != original.muellentsorgung ||
        aufzug != original.aufzug ||
        reinigungszustand != original.reinigungszustand ||
        wechselgruende != original.wechselgruende ||
        schluesselobjekt != original.schluesselobjekt ||
        alarmanlage != original.alarmanlage ||
        besonderheiten != original.besonderheiten
    } ?: (materialkammer.isNotEmpty() || waschmaschine || schmutzfangzone || wasser || strom || muelltrennung || muellentsorgung.isNotEmpty() || aufzug || reinigungszustand.isNotEmpty() || wechselgruende.isNotEmpty() || schluesselobjekt || alarmanlage || besonderheiten.isNotEmpty())

    LaunchedEffect(hasChanges) {
        onHasChangesChanged?.invoke(hasChanges)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Objektfragebogen", style = MaterialTheme.typography.headlineSmall)

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Infrastruktur", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                
                Text("Materialkammer:", style = MaterialTheme.typography.labelMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = materialkammer == "",
                        onClick = { materialkammer = "" },
                        label = { Text("k.A.") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = materialkammer == "vorhanden",
                        onClick = { materialkammer = "vorhanden" },
                        label = { Text("Vorhanden") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = materialkammer == "jede Etage",
                        onClick = { materialkammer = "jede Etage" },
                        label = { Text("Jede Etage") },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = waschmaschine,
                        onClick = { waschmaschine = !waschmaschine },
                        label = { Text(if (waschmaschine) "Waschmaschine ✓" else "Waschmaschine") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = schmutzfangzone,
                        onClick = { schmutzfangzone = !schmutzfangzone },
                        label = { Text(if (schmutzfangzone) "Schmutzfang ✓" else "Schmutzfang") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = wasser,
                        onClick = { wasser = !wasser },
                        label = { Text(if (wasser) "Wasser ✓" else "Wasser") },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = strom,
                        onClick = { strom = !strom },
                        label = { Text(if (strom) "Strom ✓" else "Strom") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = muelltrennung,
                        onClick = { muelltrennung = !muelltrennung },
                        label = { Text(if (muelltrennung) "Mülltrennung ✓" else "Mülltrennung") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = aufzug,
                        onClick = { aufzug = !aufzug },
                        label = { Text(if (aufzug) "Aufzug ✓" else "Aufzug") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = schluesselobjekt,
                        onClick = { schluesselobjekt = !schluesselobjekt },
                        label = { Text(if (schluesselobjekt) "Schlüssel ✓" else "Schlüssel") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = alarmanlage,
                        onClick = { alarmanlage = !alarmanlage },
                        label = { Text(if (alarmanlage) "Alarm ✓" else "Alarm") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Entsorgung & Zustand", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                
                StylusEditText(
                    value = muellentsorgung,
                    onValueChange = { muellentsorgung = it },
                    label = "Müllentsorgung",
                    modifier = Modifier.fillMaxWidth(),
                    imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                    onImeNext = { editTextReinigungszustand.value?.apply { post { requestFocus() } } },
                    onViewCreated = { editTextMuellentsorgung.value = it }
                )
                
                StylusEditText(
                    value = reinigungszustand,
                    onValueChange = { reinigungszustand = it },
                    label = "Reinigungszustand",
                    modifier = Modifier.fillMaxWidth(),
                    imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                    onImeNext = { editTextWechselgruende.value?.apply { post { requestFocus() } } },
                    onViewCreated = { editTextReinigungszustand.value = it }
                )
                
                StylusEditText(
                    value = wechselgruende,
                    onValueChange = { wechselgruende = it },
                    label = "Gründe für Wechsel",
                    modifier = Modifier.fillMaxWidth(),
                    imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                    onImeNext = { editTextBesonderheiten.value?.apply { post { requestFocus() } } },
                    onViewCreated = { editTextWechselgruende.value = it }
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Sonstiges", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                
                StylusEditText(
                    value = besonderheiten,
                    onValueChange = { besonderheiten = it },
                    label = "Besonderheiten",
                    modifier = Modifier.fillMaxWidth(),
                    imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_DONE,
                    onImeDone = { },
                    onViewCreated = { editTextBesonderheiten.value = it }
                )
            }
        }

        Button(
            onClick = {
                val fragebogen = ObjektfragebogenEntity(
                    id = objektfragebogen?.id ?: 0,
                    aufmassId = aufmassId,
                    materialkammer = materialkammer,
                    waschmaschine = waschmaschine,
                    schmutzfangzone = schmutzfangzone,
                    wasser = wasser,
                    strom = strom,
                    muelltrennung = muelltrennung,
                    muellentsorgung = muellentsorgung,
                    aufzug = aufzug,
                    reinigungszustand = reinigungszustand,
                    wechselgruende = wechselgruende,
                    schluesselobjekt = schluesselobjekt,
                    alarmanlage = alarmanlage,
                    besonderheiten = besonderheiten
                )
                scope.launch {
                    if (objektfragebogen == null) {
                        repository.insert(fragebogen)
                    } else {
                        repository.update(fragebogen)
                    }
                    objektfragebogen = fragebogen
                    originalFragebogen = fragebogen
                    onHasChangesChanged?.invoke(false)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Speichern")
        }
    }
}

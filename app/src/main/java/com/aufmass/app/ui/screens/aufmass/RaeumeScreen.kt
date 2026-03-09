package com.aufmass.app.ui.screens.aufmass

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.text.InputType
import android.text.TextWatcher
import android.text.Editable
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aufmass.app.data.local.entity.AufmassEntity
import com.aufmass.app.data.local.entity.BodenbelagEntity
import com.aufmass.app.data.local.entity.RaumEntity
import com.aufmass.app.data.local.entity.RaumartEntity
import com.aufmass.app.data.local.entity.RhythmusEntity
import com.aufmass.app.data.repository.BodenbelagRepository
import com.aufmass.app.data.repository.RaumRepository
import com.aufmass.app.data.repository.RaumartRepository
import com.aufmass.app.data.repository.RhythmusRepository
import com.aufmass.app.ui.components.StylusEditText
import com.aufmass.app.util.NumberFormatter
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RaeumeScreen(
    aufmassId: Long,
    aufmass: AufmassEntity?,
    raumRepository: RaumRepository,
    raumartRepository: RaumartRepository,
    bodenbelagRepository: BodenbelagRepository,
    rhythmusRepository: RhythmusRepository
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    
    val raumarten by raumartRepository.getAllRaumarten().collectAsState(initial = emptyList())
    val bodenbelage by bodenbelagRepository.getAllBodenbelage().collectAsState(initial = emptyList())
    val rhysmen by rhythmusRepository.getAllRhythmen().collectAsState(initial = emptyList())
    val raeume by raumRepository.getRaeumeByAufmassId(aufmassId).collectAsState(initial = emptyList())

    var raumbezeichnung by remember { mutableStateOf("") }
    var selectedBodenbelag by remember { mutableStateOf<BodenbelagEntity?>(null) }
    var anzahl by remember { mutableStateOf("1") }
    var laenge by remember { mutableStateOf("") }
    var breite by remember { mutableStateOf("") }
    var schnittvorgabe by remember { mutableStateOf("") }
    var selectedRhythmus by remember { mutableStateOf<RhythmusEntity?>(null) }
    var notizen by remember { mutableStateOf("") }
    var fotoUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var showPhotoDialog by remember { mutableStateOf(false) }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var zusatzlicheMasse by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var showMassDialog by remember { mutableStateOf(false) }
    var newMassLaenge by remember { mutableStateOf("") }
    var newMassBreite by remember { mutableStateOf("") }
    
    var editingRaum by remember { mutableStateOf<RaumEntity?>(null) }
    var showRhythmusDropdown by remember { mutableStateOf(false) }

    val editTextSchnitt = remember { mutableStateOf<EditText?>(null) }
    val editTextLaenge = remember { mutableStateOf<EditText?>(null) }
    val editTextBreite = remember { mutableStateOf<EditText?>(null) }
    val editTextNotizen = remember { mutableStateOf<EditText?>(null) }

    val defaultRhythmus = aufmass?.standardrhythmus

    var currentPhotoFile by remember { mutableStateOf<File?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && currentPhotoFile != null) {
            fotoUris = fotoUris + Uri.fromFile(currentPhotoFile)
        } else {
            if (fotoUris.isNotEmpty()) {
                fotoUris = fotoUris.dropLast(1)
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            try {
                val (uri, file) = createImageUriForCamera(context, aufmass?.titel ?: "unbekannt")
                currentPhotoFile = file
                takePictureLauncher.launch(uri)
            } catch (e: Exception) {
                Toast.makeText(context, "Fehler beim Erstellen der Kamera: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Kamera-Berechtigung erforderlich", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePhoto() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            try {
                val (uri, file) = createImageUriForCamera(context, aufmass?.titel ?: "unbekannt")
                currentPhotoFile = file
                takePictureLauncher.launch(uri)
            } catch (e: Exception) {
                Toast.makeText(context, "Fehler: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun resetForm() {
        raumbezeichnung = ""
        selectedBodenbelag = null
        anzahl = "1"
        laenge = ""
        breite = ""
        schnittvorgabe = ""
        selectedRhythmus = null
        notizen = ""
        fotoUris = emptyList()
        zusatzlicheMasse = emptyList()
        editingRaum = null
        focusManager.clearFocus()
        scope.launch {
            scrollState.animateScrollTo(0)
        }
    }

    fun editRaum(raum: RaumEntity) {
        editingRaum = raum
        raumbezeichnung = raum.name
        selectedBodenbelag = bodenbelage.find { it.bezeichnung == raum.bodenbelag }
        anzahl = raum.anzahl.toString()
        laenge = if (raum.laenge > 0) NumberFormatter.formatDecimal(raum.laenge) else ""
        breite = if (raum.breite > 0) NumberFormatter.formatDecimal(raum.breite) else ""
        schnittvorgabe = if (raum.schnittvorgabe >= 0) NumberFormatter.formatDecimal(raum.schnittvorgabe) else ""
        selectedRhythmus = rhysmen.find { it.klartext == raum.rhythmus }
        notizen = raum.notizen
        fotoUris = if (raum.fotoPaths.isNotBlank()) {
            raum.fotoPaths.split("|").filter { it.isNotBlank() }.map { path ->
                val uri = Uri.parse(path)
                if (path.startsWith("content://") || !File(path).exists()) {
                    val file = File(path)
                    if (file.exists()) Uri.fromFile(file) else uri
                } else {
                    uri
                }
            }
        } else {
            emptyList()
        }
        zusatzlicheMasse = if (raum.zusatzlicheMasse.isNotBlank()) {
            raum.zusatzlicheMasse.split(";").mapNotNull { mass ->
                val parts = mass.trim().split("x")
                if (parts.size == 2) {
                    val l = NumberFormatter.formatDecimal(parts[0].toDoubleOrNull() ?: 0.0)
                    val b = NumberFormatter.formatDecimal(parts[1].toDoubleOrNull() ?: 0.0)
                    l to b
                } else null
            }
        } else {
            emptyList()
        }
    }

    fun saveRaum() {
        if (raumbezeichnung.isBlank() || laenge.isBlank() || breite.isBlank()) {
            Toast.makeText(context, "Pflichtfelder ausfüllen", Toast.LENGTH_SHORT).show()
            return
        }
        
        scope.launch {
            try {
                val fotoPathsStr = fotoUris.joinToString("|") { it.toString() }
                val zusatzlicheMasseStr = zusatzlicheMasse.joinToString(";") { "${it.first}x${it.second}" }
                val raum = RaumEntity(
                    id = editingRaum?.id ?: 0,
                    aufmassId = aufmassId,
                    name = raumbezeichnung,
                    raumart = raumarten.find { raumbezeichnung.contains(it.bezeichnung, ignoreCase = true) }?.bezeichnung ?: raumbezeichnung,
                    bodenbelag = selectedBodenbelag?.bezeichnung ?: "",
                    anzahl = anzahl.toIntOrNull() ?: 1,
                    laenge = NumberFormatter.parseDecimal(laenge) ?: 0.0,
                    breite = NumberFormatter.parseDecimal(breite) ?: 0.0,
                    schnittvorgabe = NumberFormatter.parseDecimal(schnittvorgabe) ?: 0.0,
                    rhythmus = selectedRhythmus?.klartext ?: defaultRhythmus ?: "",
                    notizen = notizen,
                    fotoPaths = fotoPathsStr,
                    zusatzlicheMasse = zusatzlicheMasseStr
                )
                if (editingRaum == null) raumRepository.insert(raum) else raumRepository.update(raum)
                keyboardController?.hide()
                resetForm()
            } catch (e: Exception) {
                Toast.makeText(context, "Fehler beim Speichern: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp).imePadding()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(if (editingRaum == null) "Neuen Raum anlegen" else "Bearbeiten", style = MaterialTheme.typography.labelLarge)
                    
                    AndroidView(
                        factory = { context ->
                            EditText(context).apply {
                                hint = "Raumbezeichnung *"
                                setText(raumbezeichnung)
                                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    setMargins(0, 8, 0, 8)
                                }
                                addTextChangedListener(object : android.text.TextWatcher {
                                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                                    override fun afterTextChanged(s: Editable?) {
                                        val text = s?.toString() ?: ""
                                        raumbezeichnung = text
                                        val match = raumarten.find { text.equals(it.bezeichnung, ignoreCase = true) }
                                        if (match != null && schnittvorgabe.isBlank()) {
                                            schnittvorgabe = NumberFormatter.formatDecimal(match.schnittvorgabe)
                                        }
                                    }
                                })
                            }
                        },
                        update = { editText ->
                            if (editText.text.toString() != raumbezeichnung) {
                                editText.setText(raumbezeichnung)
                                editText.setSelection(raumbezeichnung.length)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Bodenbelag:", style = MaterialTheme.typography.labelSmall)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        bodenbelage.forEach { bodenbelag ->
                            FilterChip(
                                selected = selectedBodenbelag?.id == bodenbelag.id,
                                onClick = { selectedBodenbelag = if (selectedBodenbelag?.id == bodenbelag.id) null else bodenbelag },
                                label = { Text(bodenbelag.abkuerzung.ifBlank { bodenbelag.bezeichnung }) }
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Anz: ", style = MaterialTheme.typography.labelMedium)
                        Text("1", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.width(4.dp))
                        Text("(in Liste änderbar)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(8.dp))
                        StylusEditText(
                            value = schnittvorgabe,
                            onValueChange = { schnittvorgabe = it },
                            label = "Schnitt",
                            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                            allowComma = true,
                            imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                            onImeNext = { editTextLaenge.value?.apply { post { requestFocus() } } },
                            onViewCreated = { editTextSchnitt.value = it },
                            modifier = Modifier.weight(0.8f)
                        )
                        ExposedDropdownMenuBox(
                            expanded = showRhythmusDropdown,
                            onExpandedChange = { showRhythmusDropdown = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedRhythmus?.klartext ?: defaultRhythmus ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Rhythmus") },
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
                                        onClick = { selectedRhythmus = rhythmus; showRhythmusDropdown = false }
                                    )
                                }
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        StylusEditText(
                            value = laenge,
                            onValueChange = { laenge = it },
                            label = "Länge *",
                            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                            allowComma = true,
                            imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                            onImeNext = { editTextBreite.value?.apply { post { requestFocus() } } },
                            onViewCreated = { editTextLaenge.value = it },
                            modifier = Modifier.weight(1f)
                        )
                        StylusEditText(
                            value = breite,
                            onValueChange = { breite = it },
                            label = "Breite *",
                            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                            allowComma = true,
                            imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                            onImeNext = { editTextNotizen.value?.apply { post { requestFocus() } } },
                            onViewCreated = { editTextBreite.value = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (zusatzlicheMasse.isNotEmpty()) {
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Zusätzliche Maße:", style = MaterialTheme.typography.labelSmall)
                                zusatzlicheMasse.forEachIndexed { index, (l, b) ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val flaeche = (NumberFormatter.parseDecimal(l) ?: 0.0) * (NumberFormatter.parseDecimal(b) ?: 0.0) * (anzahl.toIntOrNull() ?: 1)
                                        Text("${l}×${b}m = ${NumberFormatter.formatFlaeche(flaeche)}")
                                        IconButton(onClick = { zusatzlicheMasse = zusatzlicheMasse - zusatzlicheMasse[index] }) {
                                            Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    TextButton(onClick = { showMassDialog = true }) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Weiteres Maß hinzufügen")
                    }

                    StylusEditText(
                        value = notizen,
                        onValueChange = { notizen = it },
                        label = "Notizen",
                        imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_DONE,
                        onImeDone = { saveRaum() },
                        onViewCreated = { editTextNotizen.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (fotoUris.isNotEmpty()) {
                            val lastUri = fotoUris.last()
                            Card(modifier = Modifier.size(48.dp)) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(File(lastUri.path ?: ""))
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Foto Vorschau",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        OutlinedButton(onClick = { takePhoto() }, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.CameraAlt, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Foto (${fotoUris.size})")
                        }
                        if (fotoUris.isNotEmpty()) {
                            OutlinedButton(onClick = { showPhotoDialog = true }, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.Photo, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Ansehen")
                            }
                        }
                        Button(
                            onClick = { saveRaum() },
                            modifier = Modifier.weight(1f),
                            enabled = raumbezeichnung.isNotBlank() && laenge.isNotBlank() && breite.isNotBlank()
                        ) {
                            Icon(Icons.Default.Save, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text(if (editingRaum == null) "Speichern" else "Update")
                        }
                        if (editingRaum != null) OutlinedButton(onClick = { resetForm() }) { Text("X") }
                    }
                }
            }
        }

        if (showPhotoDialog) {
            if (selectedPhotoUri != null) {
                AlertDialog(
                    onDismissRequest = { selectedPhotoUri = null },
                    title = { Text("Foto") },
                    text = {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(File(selectedPhotoUri?.path ?: ""))
                                .crossfade(true)
                                .build(),
                            contentDescription = "Foto Vollbild",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f)
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { selectedPhotoUri = null }) { Text("Schließen") }
                    }
                )
            } else {
                AlertDialog(
                    onDismissRequest = { showPhotoDialog = false },
                    title = { Text("Fotos (${fotoUris.size})") },
                    text = {
                        if (fotoUris.isEmpty()) {
                            Text("Keine Fotos vorhanden")
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                fotoUris.forEachIndexed { index, uri ->
                                    Card(
                                        onClick = { selectedPhotoUri = uri },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(context)
                                                    .data(File(uri.path ?: ""))
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = "Foto ${index + 1}",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.size(60.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("Foto ${index + 1}", style = MaterialTheme.typography.bodyMedium)
                                            Spacer(modifier = Modifier.weight(1f))
                                            IconButton(onClick = { fotoUris = fotoUris - uri }) {
                                                Icon(Icons.Default.Delete, "Löschen", tint = MaterialTheme.colorScheme.error)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showPhotoDialog = false }) { Text("Schließen") }
                    }
                )
            }
        }

        if (showMassDialog) {
            AlertDialog(
                onDismissRequest = { showMassDialog = false },
                title = { Text("Weiteres Maß hinzufügen") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Zusätzliches Maß für verwinkelte Räume", style = MaterialTheme.typography.labelSmall)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StylusEditText(
                                value = newMassLaenge,
                                onValueChange = { newMassLaenge = it },
                                label = "Länge (m)",
                                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                            allowComma = true,
                                modifier = Modifier.weight(1f)
                            )
                            StylusEditText(
                                value = newMassBreite,
                                onValueChange = { newMassBreite = it },
                                label = "Breite (m)",
                                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                            allowComma = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newMassLaenge.isNotBlank() && newMassBreite.isNotBlank()) {
                                zusatzlicheMasse = zusatzlicheMasse + (newMassLaenge to newMassBreite)
                                newMassLaenge = ""
                                newMassBreite = ""
                                showMassDialog = false
                            }
                        },
                        enabled = newMassLaenge.isNotBlank() && newMassBreite.isNotBlank()
                    ) { Text("Hinzufügen") }
                },
                dismissButton = {
                    TextButton(onClick = { showMassDialog = false }) { Text("Abbrechen") }
                }
            )
        }

        Divider()

        Text("Räume (${raeume.size}):", style = MaterialTheme.typography.labelMedium)
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(raeume, key = { it.id }) { raum ->
                Card {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(raum.name, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val raumAnzahl = raum.anzahl
                                    val flaecheText = if (raum.zusatzlicheMasse.isNotBlank()) {
                                        "${raum.laenge}×${raum.breite}m + ${raum.zusatzlicheMasse.split(";").size} weitere = ${NumberFormatter.formatFlaeche(raum.gesamtflaeche)}"
                                    } else {
                                        "${raum.laenge}×${raum.breite}m = ${NumberFormatter.formatFlaeche(raum.flaeche)}"
                                    }
                                    Text("${raum.bodenbelag} | ${raumAnzahl}x $flaecheText", style = MaterialTheme.typography.labelSmall, maxLines = 1)
                                    if (raum.fotoPaths.isNotBlank()) {
                                        val photoCount = raum.fotoPaths.split("|").filter { it.isNotBlank() }.size
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.Photo, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                        Text(" $photoCount", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { scope.launch { raumRepository.update(raum.copy(anzahl = (raum.anzahl - 1).coerceAtLeast(1))) } }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Remove, null, modifier = Modifier.size(18.dp)) }
                                Text("${raum.anzahl}", style = MaterialTheme.typography.labelLarge)
                                IconButton(onClick = { scope.launch { raumRepository.update(raum.copy(anzahl = raum.anzahl + 1)) } }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp)) }
                                IconButton(onClick = { editRaum(raum) }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp)) }
                                IconButton(onClick = { scope.launch { raumRepository.delete(raum) } }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp)) }
                            }
                        }
                    }
                }
            }
        }
    }
}

internal fun createImageUri(context: Context, aufmassTitel: String): Uri {
    val sanitizedTitel = aufmassTitel.replace(Regex("[^a-zA-Z0-9äöüÄÖÜß]"), "_").take(30)
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${sanitizedTitel}_raum_${System.currentTimeMillis()}.jpg")
    return Uri.fromFile(file)
}

private fun createImageUriForCamera(context: Context, aufmassTitel: String): Pair<Uri, File> {
    val sanitizedTitel = aufmassTitel.replace(Regex("[^a-zA-Z0-9äöüÄÖÜß]"), "_").take(30)
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${sanitizedTitel}_raum_${System.currentTimeMillis()}.jpg")
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    return uri to file
}

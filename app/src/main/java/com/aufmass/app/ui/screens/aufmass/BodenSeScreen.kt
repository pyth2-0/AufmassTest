package com.aufmass.app.ui.screens.aufmass

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aufmass.app.bluetooth.BluetoothManager
import com.aufmass.app.bluetooth.BluetoothMeasurementHandler
import com.aufmass.app.data.local.entity.BodenSeEntity
import com.aufmass.app.data.repository.BodenSeRepository
import com.aufmass.app.ui.components.StylusEditText
import com.aufmass.app.util.NumberFormatter
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BodenSeScreen(
    aufmassId: Long,
    aufmassTitel: String,
    bodenSeRepository: BodenSeRepository
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    
    val bodenSeList by bodenSeRepository.getBodenSeByAufmassId(aufmassId).collectAsState(initial = emptyList())

    var bezeichnung by remember { mutableStateOf("") }
    var bodenart by remember { mutableStateOf("") }
    var anzahl by remember { mutableStateOf("1") }
    var laenge by remember { mutableStateOf("") }
    var breite by remember { mutableStateOf("") }
    var notizen by remember { mutableStateOf("") }
    var fotoUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var showPhotoDialog by remember { mutableStateOf(false) }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var editingBodenSe by remember { mutableStateOf<BodenSeEntity?>(null) }

    val editTextBezeichnung = remember { mutableStateOf<EditText?>(null) }
    val editTextBodenart = remember { mutableStateOf<EditText?>(null) }
    val editTextLaenge = remember { mutableStateOf<EditText?>(null) }
    val editTextBreite = remember { mutableStateOf<EditText?>(null) }
    val editTextNotizen = remember { mutableStateOf<EditText?>(null) }

    val bluetoothManager = remember { BluetoothManager.getInstance(context) }
    val measurementHandler = remember { BluetoothMeasurementHandler.getInstance(context) }
    var bluetoothConnected by remember { mutableStateOf(false) }
    var lastMeasurement by remember { mutableStateOf<Double?>(null) }

    LaunchedEffect(bluetoothManager.connectionState) {
        bluetoothManager.connectionState.observeForever { state ->
            bluetoothConnected = state == BluetoothManager.ConnectionState.Connected
        }
    }

    LaunchedEffect(bluetoothManager.lastMeasurement) {
        bluetoothManager.lastMeasurement.observeForever { measurement ->
            if (measurement != null && measurementHandler.isAutoJumpEnabled()) {
                lastMeasurement = measurement.value
                val field = measurementHandler.onMeasurementReceived(measurement.value)
                
                when (field) {
                    BluetoothMeasurementHandler.MeasurementField.LENGTH -> {
                        if (laenge.isBlank()) {
                            laenge = NumberFormatter.formatDecimal(measurement.value)
                            editTextLaenge.value?.apply { post { requestFocus() } }
                        }
                    }
                    BluetoothMeasurementHandler.MeasurementField.WIDTH -> {
                        breite = NumberFormatter.formatDecimal(measurement.value)
                        editTextBreite.value?.apply { post { requestFocus() } }
                    }
                    else -> {}
                }
            } else if (measurement != null) {
                lastMeasurement = measurement.value
            }
        }
    }

    var currentPhotoFile by remember { mutableStateOf<File?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && currentPhotoFile != null) {
            fotoUris = fotoUris + Uri.fromFile(currentPhotoFile)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            try {
                val (uri, file) = createImageUriForCamera(context, aufmassTitel.ifBlank { "unbekannt" })
                currentPhotoFile = file
                takePictureLauncher.launch(uri)
            } catch (e: Exception) {
                Toast.makeText(context, "Fehler: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun takePhoto() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            try {
                val (uri, file) = createImageUriForCamera(context, aufmassTitel.ifBlank { "unbekannt" })
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
        bezeichnung = ""
        bodenart = ""
        anzahl = "1"
        laenge = ""
        breite = ""
        notizen = ""
        fotoUris = emptyList()
        editingBodenSe = null
    }

    fun editBodenSe(bodenSe: BodenSeEntity) {
        editingBodenSe = bodenSe
        bezeichnung = bodenSe.bezeichnung
        bodenart = bodenSe.bodenart
        anzahl = bodenSe.anzahl.toString()
        laenge = if (bodenSe.laenge > 0) NumberFormatter.formatDecimal(bodenSe.laenge) else ""
        breite = if (bodenSe.breite > 0) NumberFormatter.formatDecimal(bodenSe.breite) else ""
        notizen = bodenSe.notizen
        fotoUris = if (bodenSe.fotoPath.isNotBlank()) {
            bodenSe.fotoPath.split("|").filter { it.isNotBlank() }.map { path ->
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
    }

    fun saveBodenSe() {
        if (bezeichnung.isBlank() || laenge.isBlank() || breite.isBlank()) {
            Toast.makeText(context, "Pflichtfelder ausfüllen", Toast.LENGTH_SHORT).show()
            return
        }
        scope.launch {
            try {
                val bodenSe = BodenSeEntity(
                    id = editingBodenSe?.id ?: 0, aufmassId = aufmassId, bezeichnung = bezeichnung,
                    bodenart = bodenart, anzahl = anzahl.toIntOrNull() ?: 1,
                    laenge = NumberFormatter.parseDecimal(laenge) ?: 0.0, breite = NumberFormatter.parseDecimal(breite) ?: 0.0,
                    notizen = notizen, fotoPath = fotoUris.joinToString("|") { it.toString() }
                )
                if (editingBodenSe == null) bodenSeRepository.insert(bodenSe) else bodenSeRepository.update(bodenSe)
                keyboardController?.hide()
                resetForm()
            } catch (e: Exception) {
                Toast.makeText(context, "Fehler: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp).imePadding()) {
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (editingBodenSe == null) "Neue Boden/S&E-Fläche" else "Bearbeiten", style = MaterialTheme.typography.labelLarge)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (lastMeasurement != null) {
                                Text(
                                    "${NumberFormatter.formatDecimal(lastMeasurement!!)}m",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                            Icon(
                                imageVector = if (bluetoothConnected) Icons.Default.Bluetooth else Icons.Default.BluetoothDisabled,
                                contentDescription = "Bluetooth",
                                tint = if (bluetoothConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    
                    StylusEditText(
                        value = bezeichnung, onValueChange = { bezeichnung = it },
                        label = "Bezeichnung *", modifier = Modifier.fillMaxWidth(),
                        imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                        onImeNext = { editTextBodenart.value?.apply { post { requestFocus() } } },
                        onViewCreated = { editTextBezeichnung.value = it }
                    )

                    StylusEditText(
                        value = bodenart, onValueChange = { bodenart = it },
                        label = "Bodenart (Freitext)", modifier = Modifier.fillMaxWidth(),
                        imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                        onImeNext = { editTextLaenge.value?.apply { post { requestFocus() } } },
                        onViewCreated = { editTextBodenart.value = it }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Anz: ", style = MaterialTheme.typography.labelMedium)
                        Text("1", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.width(4.dp))
                        Text("(in Liste änderbar)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(8.dp))
                        StylusEditText(value = laenge, onValueChange = { laenge = it },
                            label = "Länge (m) *", modifier = Modifier.weight(1f),
                            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                            allowComma = true,
                            imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                            onImeNext = { editTextBreite.value?.apply { post { requestFocus() } } },
                            onViewCreated = { editTextLaenge.value = it }
                        )
                        StylusEditText(value = breite, onValueChange = { breite = it },
                            label = "Breite (m) *", modifier = Modifier.weight(1f),
                            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                            allowComma = true,
                            imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
                            onImeNext = { editTextNotizen.value?.apply { post { requestFocus() } } },
                            onViewCreated = { editTextBreite.value = it }
                        )
                    }

                    StylusEditText(value = notizen, onValueChange = { notizen = it },
                        label = "Notizen", modifier = Modifier.fillMaxWidth(),
                        imeAction = android.view.inputmethod.EditorInfo.IME_ACTION_DONE,
                        onImeDone = { },
                        onViewCreated = { editTextNotizen.value = it }
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
                        Button(onClick = { saveBodenSe() }, modifier = Modifier.weight(1f),
                            enabled = bezeichnung.isNotBlank() && laenge.isNotBlank() && breite.isNotBlank()) {
                            Icon(Icons.Default.Save, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text(if (editingBodenSe == null) "Speichern" else "Update")
                        }
                        if (editingBodenSe != null) OutlinedButton(onClick = { resetForm() }) { Text("X") }
                    }
                }
            }
        }

        Divider()
        Text("Boden/S&E-Flächen (${bodenSeList.size}):", style = MaterialTheme.typography.labelMedium)
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(bodenSeList, key = { it.id }) { bodenSe ->
                Card {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(bodenSe.bezeichnung, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("${bodenSe.bodenart} | ${bodenSe.anzahl}x ${bodenSe.laenge}×${bodenSe.breite}m = ${NumberFormatter.formatFlaeche(bodenSe.flaeche)}", style = MaterialTheme.typography.labelSmall)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { scope.launch { bodenSeRepository.update(bodenSe.copy(anzahl = (bodenSe.anzahl - 1).coerceAtLeast(1))) } }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Remove, null, modifier = Modifier.size(18.dp)) }
                                Text("${bodenSe.anzahl}", style = MaterialTheme.typography.labelLarge)
                                IconButton(onClick = { scope.launch { bodenSeRepository.update(bodenSe.copy(anzahl = bodenSe.anzahl + 1)) } }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp)) }
                                IconButton(onClick = { editBodenSe(bodenSe) }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp)) }
                                IconButton(onClick = { scope.launch { bodenSeRepository.delete(bodenSe) } }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp)) }
                            }
                        }
                    }
                }
            }
        }

        if (showPhotoDialog) {
            if (selectedPhotoUri != null) {
                AlertDialog(
                    onDismissRequest = { selectedPhotoUri = null; showPhotoDialog = false },
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
                        TextButton(onClick = { selectedPhotoUri = null }) { Text("Zurück") }
                    },
                    dismissButton = {
                        TextButton(onClick = { 
                            fotoUris = fotoUris - selectedPhotoUri!!
                            selectedPhotoUri = null
                        }) { Text("Löschen", color = MaterialTheme.colorScheme.error) }
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
    }
}

private fun createImageUriForCamera(context: Context, aufmassTitel: String): Pair<Uri, File> {
    val sanitizedTitel = aufmassTitel.replace(Regex("[^a-zA-Z0-9äöüÄÖÜß]"), "_").take(30)
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${sanitizedTitel}_boden_${System.currentTimeMillis()}.jpg")
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    return uri to file
}

package com.aufmass.app.ui.screens.home

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aufmass.app.data.local.entity.AufmassEntity
import com.aufmass.app.ui.components.StylusEditText
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToAufmass: (Long) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val aufmassList by viewModel.aufmassList.collectAsState()
    var showNewDialog by remember { mutableStateOf(false) }
    var newAufmassTitel by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flächomat v3.15.14") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Einstellungen")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Neues Aufmaß")
            }
        }
    ) { padding ->
        if (aufmassList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Keine Aufmaße vorhanden. Erstellen Sie ein neues Aufmaß.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(aufmassList, key = { it.id }) { aufmass ->
                    AufmassCard(
                        aufmass = aufmass,
                        onClick = { onNavigateToAufmass(aufmass.id) },
                        onDelete = { viewModel.deleteAufmass(aufmass) }
                    )
                }
            }
        }

        if (showNewDialog) {
            AlertDialog(
                onDismissRequest = { showNewDialog = false },
                title = { Text("Neues Aufmaß") },
                text = {
                    StylusEditText(
                        value = newAufmassTitel,
                        onValueChange = { newAufmassTitel = it },
                        label = "Titel",
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newAufmassTitel.isNotBlank()) {
                                viewModel.createNewAufmass(newAufmassTitel) { id ->
                                    onNavigateToAufmass(id)
                                }
                                showNewDialog = false
                                newAufmassTitel = ""
                            }
                        }
                    ) {
                        Text("Erstellen")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showNewDialog = false }) {
                        Text("Abbrechen")
                    }
                }
            )
        }
    }
}

@Composable
fun AufmassCard(
    aufmass: AufmassEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = aufmass.titel,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Erstellt: ${dateFormat.format(Date(aufmass.erstelltAm))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (aufmass.objektanschrift.isNotBlank()) {
                    Text(
                        text = aufmass.objektanschrift,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Löschen",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Aufmaß löschen") },
            text = { Text("Möchten Sie das Aufmaß '${aufmass.titel}' wirklich löschen?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Löschen", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Abbrechen")
                }
            }
        )
    }
}

package com.aufmass.app.ui.screens.aufmass

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aufmass.app.data.local.entity.LvEinstellungEntity
import com.aufmass.app.data.local.entity.RaumEntity
import com.aufmass.app.data.repository.LvEinstellungRepository
import com.aufmass.app.data.repository.RhythmusRepository

@Composable
fun LvScreen(
    raeume: List<RaumEntity>,
    lvRepository: LvEinstellungRepository,
    rhythmusRepository: RhythmusRepository,
    raumarten: List<com.aufmass.app.data.local.entity.RaumartEntity>
) {
    var lvEinstellungen by remember { mutableStateOf<List<LvEinstellungEntity>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        lvRepository.getAllLvEinstellungen().collect { list ->
            lvEinstellungen = list
        }
    }
    
    val raeumeByArt = raeume.groupBy { it.raumart }
    val verwendeteRaumarten = raeumeByArt.keys.toSet()
    val lvByRaumart = lvEinstellungen.groupBy { it.raumart }
    val globalLv = lvEinstellungen.filter { it.raumart == "*" }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Leistungsverzeichnis", style = MaterialTheme.typography.headlineSmall)
        Text("Globale Vorlagen (*) gelten für alle Raumarten", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (globalLv.isNotEmpty()) {
                item {
                    ExpandableCard(title = "Globale Vorlagen", count = "${globalLv.size} Aufgaben", color = MaterialTheme.colorScheme.tertiaryContainer) {
                        globalLv.sortedBy { it.spalte }.forEach { lv ->
                            LvZeile(lv = lv)
                        }
                    }
                }
            }
            
            verwendeteRaumarten.sorted().forEach { raumart ->
                val lvZeilen = lvByRaumart[raumart] ?: emptyList()
                val raeumeDerArt = raeumeByArt[raumart] ?: emptyList()
                
                item {
                    ExpandableCard(
                        title = raumart,
                        count = "${raeumeDerArt.size} Raum/Räume • ${lvZeilen.size} Aufgaben",
                        color = MaterialTheme.colorScheme.primaryContainer,
                        isEmpty = lvZeilen.isEmpty()
                    ) {
                        if (lvZeilen.isNotEmpty()) {
                            lvZeilen.sortedBy { it.spalte }.forEach { lv ->
                                LvZeile(lv = lv)
                            }
                        } else {
                            Row(modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)) {
                                Text("→ Globale Vorlagen werden verwendet", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandableCard(
    title: String,
    count: String,
    color: androidx.compose.ui.graphics.Color,
    isEmpty: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(true) }
    
    Card(colors = CardDefaults.cardColors(containerColor = color)) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium)
                    Text(count, style = MaterialTheme.typography.bodySmall)
                }
                if (isEmpty) {
                    Icon(Icons.Default.Warning, contentDescription = "Keine Aufgaben", tint = MaterialTheme.colorScheme.error)
                }
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Einklappen" else "Ausklappen"
                )
            }
            if (expanded) {
                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun LvZeile(lv: LvEinstellungEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(lv.spalte, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.15f))
        Text(lv.aufgabe, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1.3f))
        Text(lv.rhythmusPlatzhalter, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(0.55f))
    }
}

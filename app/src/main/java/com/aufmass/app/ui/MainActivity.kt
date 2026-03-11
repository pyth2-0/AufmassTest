package com.aufmass.app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aufmass.app.AufmassApplication
import com.aufmass.app.bluetooth.BluetoothManager
import com.aufmass.app.bluetooth.BluetoothSettingsManager
import com.aufmass.app.data.repository.*
import com.aufmass.app.ui.navigation.Screen
import com.aufmass.app.ui.screens.aufmass.*
import com.aufmass.app.ui.screens.home.HomeScreen
import com.aufmass.app.ui.screens.home.HomeViewModel
import com.aufmass.app.ui.screens.settings.SettingsScreen
import com.aufmass.app.ui.screens.settings.SettingsViewModel
import com.aufmass.app.ui.theme.AufmassAppTheme
import com.aufmass.app.util.ExcelExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    
    override fun onResume() {
        super.onResume()
        // Auto-Connect für Bluetooth beim App-Start und wenn App wieder aufgerufen wird
        val bluetoothSettings = BluetoothSettingsManager.getInstance(this)
        
        if (bluetoothSettings.autoConnect) {
            if (bluetoothSettings.isConfigured()) {
                val bluetoothManager = BluetoothManager.getInstance(this)
                if (!bluetoothManager.isConnected()) {
                    Toast.makeText(this, "Verbinde mit Messgerät...", Toast.LENGTH_SHORT).show()
                    bluetoothManager.tryAutoConnect()
                }
            } else {
                Toast.makeText(this, "Keine MAC-Adresse gespeichert", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val app = application as AufmassApplication
        val database = app.database
        
        val aufmassRepository = AufmassRepository(database.aufmassDao())
        val raumRepository = RaumRepository(database.raumDao())
        val glasRepository = GlasRepository(database.glasDao())
        val bodenSeRepository = BodenSeRepository(database.bodenSeDao())
        val raumartRepository = RaumartRepository(database.raumartDao())
        val rhythmusRepository = RhythmusRepository(database.rhythmusDao())
        val bodenbelagRepository = BodenbelagRepository(database.bodenbelagDao())
        val glasartRepository = GlasartRepository(database.glasartDao())
        val lvEinstellungRepository = LvEinstellungRepository(database.lvEinstellungDao())
        val objektfragebogenRepository = ObjektfragebogenRepository(database.objektfragebogenDao())
        
        val excelExporter = ExcelExporter(this)
        
        setContent {
            AufmassAppTheme {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    composable(Screen.Home.route) {
                        val homeViewModel: HomeViewModel = viewModel(
                            factory = HomeViewModel.Factory(aufmassRepository)
                        )
                        HomeScreen(
                            viewModel = homeViewModel,
                            onNavigateToAufmass = { id ->
                                navController.navigate(Screen.AufmassDetail.createRoute(id))
                            },
                            onNavigateToSettings = {
                                navController.navigate(Screen.Settings.route)
                            }
                        )
                    }
                    
                    composable(Screen.Settings.route) {
                        val settingsViewModel: SettingsViewModel = viewModel(
                            factory = SettingsViewModel.Factory(
                                raumartRepository,
                                rhythmusRepository,
                                bodenbelagRepository,
                                glasartRepository,
                                lvEinstellungRepository
                            )
                        )
                        SettingsScreen(
                            viewModel = settingsViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    
                    composable(
                        route = Screen.AufmassDetail.route,
                        arguments = listOf(navArgument("aufmassId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val aufmassId = backStackEntry.arguments?.getLong("aufmassId") ?: return@composable
                        val aufmassDetailViewModel: AufmassDetailViewModel = viewModel(
                            factory = AufmassDetailViewModel.Factory(
                                aufmassId,
                                aufmassRepository,
                                raumRepository,
                                glasRepository,
                                bodenSeRepository
                            )
                        )
                        AufmassDetailScreen(
                            viewModel = aufmassDetailViewModel,
                            raumRepository = raumRepository,
                            raumartRepository = raumartRepository,
                            bodenbelagRepository = bodenbelagRepository,
                            rhythmusRepository = rhythmusRepository,
                            glasRepository = glasRepository,
                            glasartRepository = glasartRepository,
                            bodenSeRepository = bodenSeRepository,
                            objektfragebogenRepository = objektfragebogenRepository,
                            lvEinstellungRepository = lvEinstellungRepository,
                            onNavigateBack = { navController.popBackStack() },
                            onExport = {
                                val uiState = aufmassDetailViewModel.uiState.value
                                uiState.aufmass?.let { aufmass ->
                                    lifecycleScope.launch {
                                        try {
                                            val file = withContext(Dispatchers.IO) {
                                                excelExporter.exportAufmass(
                                                    aufmass,
                                                    uiState.raeume,
                                                    uiState.glasList,
                                                    uiState.bodenSeList
                                                )
                                            }
                                            startActivity(excelExporter.shareFile(file))
                                        } catch (e: Exception) {
                                            Toast.makeText(this@MainActivity, "Export-Fehler: ${e.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

package com.willkez.calendar

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.willkez.calendar.ui.screens.*
import com.willkez.calendar.ui.theme.CalendarAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalendarAppTheme {
                var hasCalendarPermission by remember {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_CALENDAR
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                }

                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    hasCalendarPermission = permissions[Manifest.permission.READ_CALENDAR] == true
                }

                LaunchedEffect(Unit) {
                    if (!hasCalendarPermission) {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.READ_CALENDAR,
                                Manifest.permission.WRITE_CALENDAR
                            )
                        )
                    }
                }

                if (hasCalendarPermission) {
                    val navController = rememberNavController()
                    Scaffold { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "month",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("year") { YearScreen(navController) }
                            composable("month") { MonthScreen(navController) }
                            composable("week") { WeekScreen(navController) }
                            composable("agenda") { AgendaScreen(navController) }
                            composable("event_info/{eventId}") { backStackEntry ->
                                val eventId = backStackEntry.arguments?.getString("eventId")
                                EventInfoScreen(navController, eventId)
                            }
                            composable("new_event") { NewEventScreen(navController) }
                            composable("settings") { SettingsScreen(navController) }
                        }
                    }
                }
            }
        }
    }
}

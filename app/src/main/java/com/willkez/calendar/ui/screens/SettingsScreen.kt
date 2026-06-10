package com.willkez.calendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.willkez.calendar.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDark, titleContentColor = White, navigationIconContentColor = White)
            )
        },
        containerColor = BgDark
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsSection("Account Management") {
                SettingsRow("User Account", "example@gmail.com")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection("Display") {
                ToggleRow("Show Week Numbers", true)
                SettingsRow("Start Week On", "Sunday")
                ToggleRow("Set Time Zone", true)
                SettingsRow("Select Time Zone", "GMT +3:00 East Africa Time")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection("Region") {
                SettingsRow("National/Regional Holidays", "Tanzania")
                SettingsRow("Religious Holidays", "Christian Holidays")
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text("Delete All Overdue Events", color = androidx.compose.ui.graphics.Color.Red)
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(title, color = TextMuted, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
        Surface(color = CardDark, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun ToggleRow(title: String, checked: Boolean) {
    var isChecked by remember { mutableStateOf(checked) }
    Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = White)
        Switch(checked = isChecked, onCheckedChange = { isChecked = it })
    }
}

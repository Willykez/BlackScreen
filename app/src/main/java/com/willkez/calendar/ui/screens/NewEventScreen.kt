package com.willkez.calendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.willkez.calendar.data.CalendarRepository
import com.willkez.calendar.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = remember { CalendarRepository(context) }

    var title by remember { mutableStateOf("") }
    var allDay by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New", fontSize = 18.sp) },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDark, titleContentColor = White, actionIconContentColor = White)
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
            Row {
                FilterChip(
                    selected = true,
                    onClick = {},
                    label = { Text("Event") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AccentBlue, selectedLabelColor = White)
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = false,
                    onClick = {},
                    label = { Text("Important Day") }
                )
            }

            TextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("My event", color = TextMuted) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CardDark,
                    unfocusedContainerColor = CardDark,
                    focusedTextColor = White,
                    unfocusedTextColor = White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(color = CardDark, shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("All-day", color = White)
                        Switch(checked = allDay, onCheckedChange = { allDay = it })
                    }
                    HorizontalDivider(color = White.copy(alpha = 0.1f))
                    Text("Start Time", color = White, modifier = Modifier.padding(vertical = 8.dp))
                    Text("Sat, Jun 14 02:30", color = TextMuted, fontSize = 12.sp)
                    HorizontalDivider(color = White.copy(alpha = 0.1f))
                    Text("End Time", color = White, modifier = Modifier.padding(vertical = 8.dp))
                    Text("Sat, Jun 14 03:30", color = TextMuted, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    repository.addEvent(
                        title = title,
                        description = "",
                        startTime = System.currentTimeMillis(),
                        endTime = System.currentTimeMillis() + 3600000,
                        location = "",
                        allDay = allDay
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("OK", color = White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

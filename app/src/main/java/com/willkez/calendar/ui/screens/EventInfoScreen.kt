package com.willkez.calendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.willkez.calendar.data.CalendarEvent
import com.willkez.calendar.data.CalendarRepository
import com.willkez.calendar.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventInfoScreen(navController: NavController, eventId: String?) {
    val context = LocalContext.current
    val repository = remember { CalendarRepository(context) }
    var event by remember { mutableStateOf<CalendarEvent?>(null) }

    LaunchedEffect(eventId) {
        if (eventId != null) {
            // In a real app, we'd have a fetchEventById. For now, fetch all and filter.
            event = repository.fetchEvents(0, Long.MAX_VALUE).find { it.id == eventId.toLong() }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Info", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BgDark,
                    titleContentColor = White,
                    navigationIconContentColor = White
                )
            )
        },
        containerColor = BgDark
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            event?.let {
                InfoCard(it.title, "Start: " + SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(Date(it.dtStart)))
                Spacer(modifier = Modifier.height(16.dp))
                InfoCard("Save to", "Local Calendar")
                Spacer(modifier = Modifier.height(16.dp))
                InfoCard("Remark", it.description ?: "None")
                Spacer(modifier = Modifier.height(16.dp))
                SettingsRow("Reminder Style", "No Reminder")
            } ?: Text("Event not found", color = White)
        }
    }
}

@Composable
fun InfoCard(title: String, subtitle: String) {
    Surface(
        color = CardDark,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = White)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = TextMuted
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(subtitle, color = TextMuted)
            }
        }
    }
}

@Composable
fun SettingsRow(title: String, subtitle: String) {
    Surface(
        color = CardDark,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(title, color = White, fontSize = 16.sp)
                Text(subtitle, color = TextMuted, fontSize = 14.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
        }
    }
}

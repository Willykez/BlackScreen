package com.willkez.calendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun AgendaScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = remember { CalendarRepository(context) }
    var events by remember { mutableStateOf(emptyList<CalendarEvent>()) }

    LaunchedEffect(Unit) {
        val start = Calendar.getInstance().apply { set(2024, Calendar.OCTOBER, 1) }.timeInMillis
        val end = Calendar.getInstance().apply { set(2025, Calendar.DECEMBER, 31) }.timeInMillis
        events = repository.fetchEvents(start, end)
    }

    Box(modifier = Modifier.fillMaxSize().background(BgDark)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text("October", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = White)
                Text("2024", fontSize = 14.sp, color = TextMuted)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // In a real app, we would group events by date
            items(events) { event ->
                AgendaItem(event, navController)
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("new_event") },
            containerColor = AccentPurple,
            contentColor = White,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun AgendaItem(event: CalendarEvent, navController: NavController) {
    val dateFormat = SimpleDateFormat("MMM d\nEEE", Locale.getDefault())
    val dateStr = dateFormat.format(Date(event.dtStart))

    Row(modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth()) {
        Text(
            text = dateStr,
            modifier = Modifier.width(60.dp),
            color = TextMuted,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp
        )

        Column(modifier = Modifier.weight(1f)) {
            EventTile(event, navController)
        }
    }
}

@Composable
fun EventTile(event: CalendarEvent, navController: NavController) {
    Surface(
        color = CardDark,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable {
            navController.navigate("event_info/${event.id}")
        }
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.width(4.dp).height(30.dp).background(AccentBlue))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(event.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = White)
                Text(
                    if (event.allDay) "All-day" else SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(event.dtStart)),
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

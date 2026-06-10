package com.willkez.calendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.willkez.calendar.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MonthScreen(navController: NavController) {
    val currentCalendar = remember { Calendar.getInstance() }
    val year = currentCalendar.get(Calendar.YEAR)
    val month = currentCalendar.get(Calendar.MONTH)
    val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(currentCalendar.time)
    val todayDateFormatted = SimpleDateFormat("EEE, MMM d, 'today'", Locale.getDefault()).format(currentCalendar.time)

    Box(modifier = Modifier.fillMaxSize().background(BgDark)) {
        Column {
            CalendarHeader(monthName, year.toString(), navController)
            WeekdayHeader()
            LazyColumn {
                item {
                    MonthGrid(year, month)
                }
                item {
                    PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    Text(
                        todayDateFormatted,
                        color = TextMuted,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    NoEventsView()
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("new_event") },
            containerColor = AccentPurple,
            contentColor = White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Event")
        }

        Button(
            onClick = { navController.navigate("year") },
            colors = ButtonDefaults.buttonColors(containerColor = CardDark),
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
            shape = CircleShape
        ) {
            Text("Today", color = White)
        }
    }
}

@Composable
fun CalendarHeader(title: String, subtitle: String, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = White)
            Text(subtitle, fontSize = 14.sp, color = TextMuted)
        }
        Row {
            IconButton(onClick = { }) { Icon(Icons.Default.Search, contentDescription = null, tint = White) }
            IconButton(onClick = { navController.navigate("agenda") }) { Icon(Icons.Default.ViewModule, contentDescription = null, tint = White) }
            IconButton(onClick = { navController.navigate("settings") }) { Icon(Icons.Default.MoreVert, contentDescription = null, tint = White) }
        }
    }
}

@Composable
fun WeekdayHeader() {
    val weekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Spacer(modifier = Modifier.width(24.dp))
        weekdays.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = TextMuted,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun MonthGrid(year: Int, month: Int) {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val weekNumbers = listOf(23, 24, 25, 26, 27)

    var dayCounter = 1

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        for (wIndex in 0..4) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = weekNumbers[wIndex].toString(),
                    color = White.copy(alpha = 0.24f),
                    fontSize = 11.sp,
                    modifier = Modifier.width(24.dp)
                )
                for (dIndex in 0..6) {
                    if ((wIndex == 0 && dIndex < firstDayOfWeek) || dayCounter > daysInMonth) {
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        val currentDay = dayCounter
                        val today = Calendar.getInstance()
                        val isToday = today.get(Calendar.YEAR) == year &&
                                     today.get(Calendar.MONTH) == month &&
                                     today.get(Calendar.DAY_OF_MONTH) == currentDay
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isToday) AccentPurple else Color.Transparent,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = currentDay.toString(),
                                        fontSize = 16.sp,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                        color = White
                                    )
                                }
                            }
                        }
                        dayCounter++
                    }
                }
            }
        }
    }
}

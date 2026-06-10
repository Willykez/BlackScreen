package com.willkez.calendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.willkez.calendar.ui.theme.*

@Composable
fun WeekScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(BgDark)) {
        CalendarHeader("June", "2026", navController)

        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Spacer(modifier = Modifier.width(40.dp))
            val days = listOf("7", "8", "9", "10", "11", "12", "13")
            days.forEach { day ->
                val isSelected = day == "10"
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Surface(
                        shape = CircleShape,
                        color = if (isSelected) AccentPurple else Color.Transparent,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                day,
                                color = if (isSelected) White else White.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider(color = White.copy(alpha = 0.12f), thickness = 1.dp)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(24) { hour ->
                Row(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .border(width = 0.5.dp, color = White.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = hour.toString(),
                        color = TextMuted,
                        fontSize = 12.sp,
                        modifier = Modifier.width(40.dp).padding(start = 8.dp, top = 4.dp)
                    )
                    Row(modifier = Modifier.fillMaxHeight()) {
                        for (dIndex in 0..6) {
                            val isTargetSlot = dIndex == 3 && hour == 15
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .border(width = 0.5.dp, color = White.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isTargetSlot) {
                                    Surface(
                                        color = AccentPurple.copy(alpha = 0.8f),
                                        shape = MaterialTheme.shapes.small,
                                        modifier = Modifier.fillMaxSize().padding(2.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, tint = White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.willkez.calendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.willkez.calendar.ui.theme.CardDark
import com.willkez.calendar.ui.theme.TextMuted

// Adding this to fix the missing icon error in MonthScreen.kt
@Composable
fun NoEventsView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            color = CardDark,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Placeholder for icon
                Box(modifier = Modifier.size(40.dp).background(Color.Gray))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("No events", color = TextMuted, fontSize = 14.sp)
    }
}

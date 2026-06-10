package com.willkez.calendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.willkez.calendar.ui.theme.AccentPurple
import com.willkez.calendar.ui.theme.BgDark
import com.willkez.calendar.ui.theme.White
import java.util.*

@Composable
fun YearScreen(navController: NavController) {
    val currentCalendar = remember { Calendar.getInstance() }
    val year = currentCalendar.get(Calendar.YEAR)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(16.dp)
    ) {
        Text(
            text = year.toString(),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = White
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(12) { monthIndex ->
                MonthMiniView(year, monthIndex, navController)
            }
        }
    }
}

@Composable
fun MonthMiniView(year: Int, monthIndex: Int, navController: NavController) {
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, monthIndex)
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

    Column(modifier = Modifier.clickable { navController.navigate("month") }) {
        Text(
            text = months[monthIndex],
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (monthIndex == 5) AccentPurple else White
        )
        Spacer(modifier = Modifier.height(4.dp))

        Column {
            for (week in 0..5) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (day in 0..6) {
                        val dayNum = week * 7 + day - firstDayOfWeek + 1
                        val isCurrentMonth = dayNum in 1..daysInMonth

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCurrentMonth) {
                                val today = Calendar.getInstance()
                                val isToday = today.get(Calendar.YEAR) == year &&
                                             today.get(Calendar.MONTH) == monthIndex &&
                                             today.get(Calendar.DAY_OF_MONTH) == dayNum
                                Surface(
                                    shape = CircleShape,
                                    color = if (isToday) AccentPurple else Color.Transparent,
                                    modifier = Modifier.size(8.dp)
                                ) {
                                    Text(
                                        text = dayNum.toString(),
                                        fontSize = 6.sp,
                                        color = if (isToday) White else White.copy(alpha = 0.7f),
                                        modifier = Modifier.wrapContentSize()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.willkez.calendar.data

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import java.util.TimeZone

data class CalendarEvent(
    val id: Long,
    val title: String,
    val description: String?,
    val dtStart: Long,
    val dtEnd: Long,
    val eventLocation: String?,
    val allDay: Boolean
)

class CalendarRepository(private val context: Context) {

    fun fetchEvents(startTime: Long, endTime: Long): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.ALL_DAY
        )

        val selection = "(${CalendarContract.Events.DTSTART} >= ?) AND (${CalendarContract.Events.DTSTART} <= ?)"
        val selectionArgs = arrayOf(startTime.toString(), endTime.toString())

        val cursor = context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${CalendarContract.Events.DTSTART} ASC"
        )

        cursor?.use {
            val idCol = it.getColumnIndex(CalendarContract.Events._ID)
            val titleCol = it.getColumnIndex(CalendarContract.Events.TITLE)
            val descCol = it.getColumnIndex(CalendarContract.Events.DESCRIPTION)
            val startCol = it.getColumnIndex(CalendarContract.Events.DTSTART)
            val endCol = it.getColumnIndex(CalendarContract.Events.DTEND)
            val locCol = it.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)
            val allDayCol = it.getColumnIndex(CalendarContract.Events.ALL_DAY)

            while (it.moveToNext()) {
                events.add(
                    CalendarEvent(
                        id = it.getLong(idCol),
                        title = it.getString(titleCol) ?: "No Title",
                        description = it.getString(descCol),
                        dtStart = it.getLong(startCol),
                        dtEnd = it.getLong(endCol),
                        eventLocation = it.getString(locCol),
                        allDay = it.getInt(allDayCol) == 1
                    )
                )
            }
        }
        return events
    }

    private fun getDefaultCalendarId(): Long {
        val projection = arrayOf(CalendarContract.Calendars._ID)
        val selection = "${CalendarContract.Calendars.VISIBLE} = 1"
        context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            selection,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getLong(0)
            }
        }
        return 1 // Fallback
    }

    fun addEvent(
        title: String,
        description: String,
        startTime: Long,
        endTime: Long,
        location: String,
        allDay: Boolean
    ): Long? {
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startTime)
            put(CalendarContract.Events.DTEND, endTime)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.CALENDAR_ID, getDefaultCalendarId())
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            put(CalendarContract.Events.EVENT_LOCATION, location)
            put(CalendarContract.Events.ALL_DAY, if (allDay) 1 else 0)
        }
        val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        return uri?.lastPathSegment?.toLong()
    }
}

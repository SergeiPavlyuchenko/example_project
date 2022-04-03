package example.code.some_project.util

import java.util.*

fun getCurrentDateStartCalendar(): Calendar =
    Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

fun getCurrentDateStart(): Long = getCurrentDateStartCalendar().timeInMillis
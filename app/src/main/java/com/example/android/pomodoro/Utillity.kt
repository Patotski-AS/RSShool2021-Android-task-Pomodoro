package com.example.android.pomodoro


fun stringTimeToLong(string: String): Long {
    val stringTime = string.split(":")
    var time = 0L
    for (i in 0..2) {
        when (i) {
            0 -> time += stringTime[0].toLong() * 1000L * 60L * 60L
            1 -> time += stringTime[1].toLong() * 1000L * 60L
            2 -> time += stringTime[1].toLong() * 1000L
        }
    }
    return time
}

fun longTimeToString(time: Long): String {
    return if (time <= 0L) START_TIME
    else "%02d:%02d:%02d".format(
        time / 1000 / 3600,
        time / 1000 % 3600 / 60,
        time / 1000 % 60
    )
}


private const val START_TIME = "00:00:00"

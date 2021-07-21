package com.example.android.pomodoro.timer

data class Timer(
    val id: Int,
    var startMs: Long,
    var isStarted: Boolean,
    var remainingMS: Long = startMs,
    var finishTime: Long = 0L,
)
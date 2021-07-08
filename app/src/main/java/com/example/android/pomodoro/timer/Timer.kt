package com.example.android.pomodoro.timer

data class Timer(
    val id: Int,
    var currentMs: Long,
    var isStarted: Boolean
)
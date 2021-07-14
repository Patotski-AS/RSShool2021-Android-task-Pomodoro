package com.example.android.pomodoro.timer

data class Timer(
    val id: Int,
    var startMs: Long,
    var isStarted: Boolean,
    var currentMs: Long = startMs
    )
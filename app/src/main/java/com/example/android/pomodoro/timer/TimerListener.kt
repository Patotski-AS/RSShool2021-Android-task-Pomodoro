package com.example.android.pomodoro.timer

import androidx.lifecycle.ViewModelProvider

interface TimerListener {
    fun start(timer: Timer)
    fun stop(id: Int, currentMs: Long)
    fun delete(id: Int)
    fun getCurrentMs(id: Int): Long?
}
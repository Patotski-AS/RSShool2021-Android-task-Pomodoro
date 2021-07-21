package com.example.android.pomodoro.timer

interface TimerListener {
    fun start(id: Int)
    fun stop(id: Int, currentMs: Long)
    fun delete(id: Int)
    fun update(time : Long)
}
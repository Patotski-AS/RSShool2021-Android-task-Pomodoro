package com.example.android.pomodoro.timer

interface TimerListener {

    fun start(timer: Timer)
    fun stop(id: Int, currentMs: Long)
    fun delete(id: Int)
    fun update(timer: Timer)
    fun getCurrentMs(id:Int)  : Long?

}
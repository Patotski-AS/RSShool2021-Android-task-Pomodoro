package com.example.android.pomodoro.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.android.pomodoro.timer.Timer
import com.example.android.pomodoro.timer.TimerAdapter
import com.example.android.pomodoro.timer.TimerListener

class MainActivityViewModel() : ViewModel(), TimerListener {
    private val timerAdapter = TimerAdapter(this)
    private val timers = mutableListOf<Timer>()
    private var id = 0
    private var currentTime = 0L

    private fun newTimer() {
        timers.add(Timer(id++, currentTime, false))
        timerAdapter.submitList(timers.toList())
    }

    fun getTimerAdapter(): TimerAdapter {
        return timerAdapter
    }

    private fun setTime(time: Int) {
        currentTime = time * 1000L
    }

    fun getTimers(): MutableList<Timer> {
        return timers
    }

    fun addNewTimer(startTime: String) {
        if (startTime != "") {
            setTime(startTime.toInt())
        } else setTime(0)
        newTimer()

    }

    override fun start(id: Int) {
        TODO("Not yet implemented")
    }

    override fun stop(id: Int, currentMs: Long) {
        TODO("Not yet implemented")
    }

    override fun reset(id: Int) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

}
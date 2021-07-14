package com.example.android.pomodoro.main

import android.content.ClipData
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.pomodoro.timer.Timer
import com.example.android.pomodoro.timer.TimerAdapter
import com.example.android.pomodoro.timer.TimerListener

class MainActivityViewModel : ViewModel(), TimerListener {
    private val timerAdapter = TimerAdapter(this)

    private val timers = mutableListOf<Timer>()
    private var nextId = 0
    private var isStarted = true
    private var startTime = 0L
    private var currentTime = 0L

    private fun newTimer() {
        timers.add(Timer(nextId++, currentTime, false))
        timerAdapter.submitList(timers.toList())
    }

    fun getTimerAdapter(): TimerAdapter {
        return timerAdapter
    }

    fun addNewTimer(startTime: String) {
        if (startTime != "") {
            currentTime = startTime.toLong() * 1000 * 60
            newTimer()
        }
    }

    override fun start(id: Int) {
        timers.map {
            if (it.isStarted) stop(it.id, it.currentMs)
            if (it.id == id) it.isStarted = true
        }
        timerAdapter.submitList(timers.toList())
    }

    override fun stop(id: Int, currentMs: Long) {
        timers.forEach {
            if (it.id == id) {
                currentTime = it.currentMs
                it.isStarted = false
            }
        }
        timerAdapter.submitList(timers.toList())
    }

    override fun delete(id: Int) {
        timers.remove(timers.find{ it.id==id })
        timerAdapter.submitList(timers.toList())
    }

    private companion object {
        private const val START_TIME = "00:00:00"
    }
}
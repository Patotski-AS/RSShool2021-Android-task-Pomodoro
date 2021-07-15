package com.example.android.pomodoro.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.pomodoro.longTimeToString
import com.example.android.pomodoro.timer.Timer
import com.example.android.pomodoro.timer.TimerAdapter
import com.example.android.pomodoro.timer.TimerListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel(), TimerListener {
    private val timerAdapter = TimerAdapter(this)

    private val timers = mutableListOf<Timer>()
    private var nextId = 0
    private var currentTime = 0L
    private var startTime = 0L
    private var finishTime = 0L
    private var job: Job? = null


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

    override fun start(timer: Timer) {
        timers.map {
            if (it.isStarted) stop(it.id, it.currentMs)
            if (it.id == timer.id) it.isStarted = true
        }
        createCounter(timer)
//        timerAdapter.submitList(timers.toList())
    }

    override fun stop(id: Int, currentMs: Long) {
        timers.forEach {
            if (it.id == id) {
                currentTime = it.currentMs
                it.isStarted = false
            }
        }
        job?.cancel()
        timerAdapter.submitList(timers.toList())
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timerAdapter.submitList(timers.toList())
    }

    override fun update(timer: Timer) {
        timers.map {
            if (it.id == timer.id) it.currentMs = timer.currentMs
        }
        timerAdapter.notifyDataSetChanged()
        val newList = timers.map { it }
        timerAdapter.submitList(newList)
        Log.i("MyLog", "fun update $timers ")
    }


    private fun createCounter(timer: Timer) {
        finishTime = System.currentTimeMillis() + timer.currentMs

        job = viewModelScope.launch(Dispatchers.Main) {
            while (timer.isStarted) {
               timer.currentMs = finishTime - System.currentTimeMillis()
                update(timer)
                delay(INTERVAL)
            }
        }
    }


    private companion object {
        private const val START_TIME = "00:00:00"
        private const val INTERVAL = 1000L
    }
}
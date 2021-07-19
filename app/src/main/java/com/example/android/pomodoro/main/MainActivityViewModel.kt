package com.example.android.pomodoro.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.pomodoro.timer.Timer
import com.example.android.pomodoro.timer.TimerListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel(), TimerListener {

    val timers = ArrayList<Timer>()
    val timersLiveData = MutableLiveData<MutableList<Timer>>()

    private var nextId = 0
    private var currentTime = 0L
    private var finishTime = 0L
    private var job: Job? = null

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

    fun addNewTimer(startTime: String) {
        if (startTime != "") {
            currentTime = startTime.toLong() * 1000 * 60
            timers.add(Timer(nextId++, currentTime, false))
            timersLiveData.value = timers
        }
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timersLiveData.value = timers
    }

    override fun getCurrentMs(id: Int): Long? {
        return currentTime
    }

    override fun start(timer: Timer) {
        timers.map { if (it.isStarted) it.isStarted = false }
        timers[timers.indexOf(timers.find { it.id == timer.id })].apply {
            isStarted = true
        }
        finishTime = System.currentTimeMillis() + timer.currentMs
        createCounter(timer)
    }

    override fun stop(id: Int, currentMs: Long) {
        val index = timers.indexOf(timers.find { it.id == id })
        timers[index].currentMs = currentMs
        timers[index].isStarted = false
        timersLiveData.value = timers
    }

    private fun createCounter(timer: Timer) {
        val index = timers.indexOf(timers.find { it.id == timer.id })

        job = viewModelScope.launch(Dispatchers.Main) {
            while (timer.isStarted) {
                timers[index].isStarted = timer.isStarted
                timers[index].currentMs = finishTime - System.currentTimeMillis()
                if (timers[index].currentMs <=0L) job?.cancel()
                timersLiveData.value = timers
                delay(INTERVAL)
            }
        }
    }

    private companion object {
        private const val INTERVAL = 1000L
    }

}
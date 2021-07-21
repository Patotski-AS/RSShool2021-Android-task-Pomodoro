package com.example.android.pomodoro.main

import android.util.Log
import androidx.lifecycle.*
import com.example.android.pomodoro.timer.Timer
import com.example.android.pomodoro.timer.TimerListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel(),
    TimerListener {

    var timers = ArrayList<Timer>()
    var timersLiveData = MutableLiveData<MutableList<Timer>>()

    init {
        getSaveState()
    }


    fun setSaveState() {
        savedStateHandle.set("TIMERS", timers)
    }

    private fun getSaveState() {
        Log.i("MyLog", "getSaveState timers = ${savedStateHandle.get<ArrayList<Timer>>("TIMERS")} ")

        if (savedStateHandle.get<ArrayList<Timer>>("TIMERS") != null){
            timers = savedStateHandle.get<ArrayList<Timer>>("TIMERS")!!
            timersLiveData.value = timers
            Log.i("MyLog", "getSaveState timers = $timers ")

        }
    }


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
        return timers.find { it.id == id }?.currentMs
    }

    fun getFinishTime(): Long {
        return finishTime
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

    fun getIsStarted(): Boolean {
        var isStarted = false
        timers.forEach {
            if (it.isStarted) {
                isStarted = true
                return isStarted
            }
        }
        return isStarted
    }

    private fun createCounter(timer: Timer) {
        val index = timers.indexOf(timers.find { it.id == timer.id })

        job = viewModelScope.launch(Dispatchers.Main) {
            while (timer.isStarted) {
                currentTime = finishTime - System.currentTimeMillis()
                timers[index].isStarted = timer.isStarted
                timers[index].currentMs = currentTime
                if (timers[index].currentMs <= 0L) job?.cancel()
                timersLiveData.value = timers
                delay(INTERVAL)
            }
        }
    }

    private companion object {
        private const val INTERVAL = 1000L
    }

}


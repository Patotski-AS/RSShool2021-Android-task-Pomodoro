package com.example.android.pomodoro.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.pomodoro.timer.Timer
import com.example.android.pomodoro.timer.TimerAdapter
import com.example.android.pomodoro.timer.TimerListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val timers = ArrayList<Timer>()
    val timersLiveData = MutableLiveData<MutableList<Timer>>()

    private var nextId = 0
    var currentTime = 0L
    private var startTime = 0L
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

    fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timersLiveData.value = timers
    }


    private fun update(timer: Timer) {
        val index = timers.indexOf(timers.find { it.id == timer.id })
        timers[index].isStarted = timer.isStarted
        timers[index].currentMs = timer.startMs
        Log.i("MyLog", "update $timers index = $index, timer.isStarted= ${timer.isStarted} ,timer.startMs = ${timer.startMs}   ")
        timersLiveData.value = timers
    }


    fun start(timer: Timer) {
        timers[timers.indexOf(timers.find { it.id == timer.id })].apply {
            isStarted = true
        }
        finishTime = System.currentTimeMillis() + timer.currentMs
        createCounter(timer)
    }

    private fun createCounter(timer: Timer) {
        val index = timers.indexOf(timers.find { it.id == timer.id })

        job = viewModelScope.launch(Dispatchers.Main) {
            while (timer.isStarted) {
                timers[index].isStarted = timer.isStarted
                timers[index].currentMs = finishTime - System.currentTimeMillis()
                timersLiveData.value = timers
                Log.i("MyLog", "createCounter timers = $timers ")
                delay(INTERVAL)
            }
        }
    }

//
//    override fun stop(id: Int, currentMs: Long) {
//        timers.forEach {
//            if (it.id == id) {
//                currentTime = it.currentMs
//                it.isStarted = false
//            }
//        }
//        job?.cancel()
//        val newList = timers.map { it }
//        timerAdapter.submitList(newList)
//    }
//
//
//    override fun update(timer: Timer) {
//        timers.map {
//            if (it.id == timer.id) it.currentMs = timer.currentMs
//        }
////        val newList = timers.map { it }
////        timerAdapter.submitList(newList)
//        timerAdapter.notifyItemChanged(timerAdapter.currentList.indexOf(timer))
//        Log.i("MyLog", "fun update $timers ")
//    }




    private companion object {
        private const val START_TIME = "00:00:00"
        private const val INTERVAL = 1000L
    }

}
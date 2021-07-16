package com.example.android.pomodoro.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.pomodoro.timer.Timer
import com.example.android.pomodoro.timer.TimerAdapter
import com.example.android.pomodoro.timer.TimerListener
import kotlinx.coroutines.Job

class MainActivityViewModel : ViewModel() {

    private val timers = ArrayList<Timer>()
    val timersLiveData = MutableLiveData<MutableList<Timer>>()

    private var nextId = 0
    private var currentTime = 0L
    private var startTime = 0L
    private var finishTime = 0L
    private var job: Job? = null


    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

    private fun newTimer() {
        timers.add(Timer(nextId++, currentTime, false))
        timersLiveData.value = timers
//        timersLiveData.value?.add(Timer(nextId++, currentTime, false))
//        timersLiveData.notifyObserver()
        Log.i("MyLog", "newTimer ${timersLiveData.value} ")
    }

    fun addNewTimer(startTime: String) {
        if (startTime != "") {
            currentTime = startTime.toLong() * 1000 * 60
            newTimer()
        }
    }

    fun update(list: MutableList<Timer>, timer: Timer): MutableList<Timer> {
        list.add(timer)
        return list
    }

//
//    override fun start(timer: Timer) {
//        timers.map {
//            if (it.isStarted) stop(it.id, it.currentMs)
//            if (it.id == timer.id) it.isStarted = true
//        }
//        createCounter(timer)
//        val newList = timers.map { it }
//        timerAdapter.submitList(newList)
//    }
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
//    override fun delete(id: Int) {
//        timers.remove(timers.find { it.id == id })
//        timerAdapter.submitList(timers.toList())
//    }
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


//    private fun createCounter(timer: Timer) {
//        finishTime = System.currentTimeMillis() + timer.currentMs
//
//        job = viewModelScope.launch(Dispatchers.Main) {
//            while (timer.isStarted) {
//               timer.currentMs = finishTime - System.currentTimeMillis()
//                update(timer)
//                delay(INTERVAL)
//            }
//        }
//    }


//    tim.observe(viewLifecycleOwner, Observer {
//        it?.let {
//            adapter.submitList(it)
//        }
//    })


    private companion object {
        private const val START_TIME = "00:00:00"
        private const val INTERVAL = 1000L
    }

}
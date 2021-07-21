package com.example.android.pomodoro.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.pomodoro.COMMAND_ID
import com.example.android.pomodoro.COMMAND_START
import com.example.android.pomodoro.COMMAND_STOP
import com.example.android.pomodoro.STARTED_TIMER_TIME_MS
import com.example.android.pomodoro.databinding.ActivityMainBinding
import com.example.android.pomodoro.service.ForegroundService
import com.example.android.pomodoro.timer.Timer
import com.example.android.pomodoro.timer.TimerAdapter
import com.example.android.pomodoro.timer.TimerListener

class MainActivity : AppCompatActivity(), TimerListener, LifecycleObserver {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private val timerAdapter = TimerAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this, SavedStateViewModelFactory(application, this)).get(
            MainActivityViewModel::class.java
        )

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timerAdapter
        }

        viewModel.timersLiveData.observe({ lifecycle }, {
            it?.let {
                timerAdapter.submitList(it.toList())
            }
        })

        binding.addNewTimerButton.setOnClickListener {
            val time = binding.startTime.text.toString()
            viewModel.addNewTimer(time)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        val startIntent = Intent(this, ForegroundService::class.java)
        startIntent.putExtra(COMMAND_ID, COMMAND_START)
        startIntent.putExtra(STARTED_TIMER_TIME_MS, viewModel.getFinishTime())
        startService(startIntent)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }

    override fun start(timer: Timer) {
        viewModel.start(timer)
    }

    override fun stop(id: Int, currentMs: Long) {
        viewModel.stop(id, currentMs)
    }

    override fun delete(id: Int) {
        viewModel.delete(id)
    }

    override fun getCurrentMs(id: Int): Long? {
        return viewModel.timers.find { it.id == id }?.currentMs
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.setSaveState()
        Log.i("MyLog", "onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val x = savedInstanceState.getInt("INT")
        Log.i("MyLog", "onRestoreInstanceState  x = $x")
    }
}
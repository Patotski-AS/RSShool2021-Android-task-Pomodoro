package com.example.android.pomodoro.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.pomodoro.databinding.ActivityMainBinding
import com.example.android.pomodoro.timer.Timer
import com.example.android.pomodoro.timer.TimerAdapter
import com.example.android.pomodoro.timer.TimerListener

class MainActivity : AppCompatActivity(),TimerListener {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var viewModel: MainActivityViewModel
    private val timerAdapter = TimerAdapter(this)
    private var nextId = 0
    private val timers = mutableListOf<Timer>()
    private var currentTime = 0L



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timerAdapter
        }


        binding.addNewTimerButton.setOnClickListener {
            val time = binding.startTime.text.toString()
            addNewTimer(time)
        }
    }

    private fun addNewTimer(startTime: String) {
        if (startTime != "") {
            timers.add(Timer(nextId++, startTime.toLong() * 1000 * 60, false))
            timerAdapter.submitList(timers.toList())
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

}
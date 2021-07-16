package com.example.android.pomodoro.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.pomodoro.databinding.ActivityMainBinding
import com.example.android.pomodoro.timer.Timer
import com.example.android.pomodoro.timer.TimerAdapter
import com.example.android.pomodoro.timer.TimerListener

class MainActivity : AppCompatActivity(),TimerListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private val timerAdapter = TimerAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timerAdapter
        }

        viewModel.timersLiveData.observe(this, {
            it?.let { timerAdapter.submitList(it)
                Log.i("MyLog", "viewModel.timers.observe $it")
            }
        })

        binding.addNewTimerButton.setOnClickListener {
            val time = binding.startTime.text.toString()
            viewModel.addNewTimer(time)
        }
    }

    override fun start(timer: Timer) {
        TODO("Not yet implemented")
    }

    override fun stop(id: Int, currentMs: Long) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun update(timer: Timer) {
        TODO("Not yet implemented")
    }

}
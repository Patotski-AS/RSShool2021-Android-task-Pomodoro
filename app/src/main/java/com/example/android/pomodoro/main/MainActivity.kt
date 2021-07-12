package com.example.android.pomodoro.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.pomodoro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModel.getTimerAdapter()
        }


        binding.addNewTimerButton.setOnClickListener {
            val time = binding.startTime.text.toString()
            viewModel.addNewTimer(time)
        }
    }
}
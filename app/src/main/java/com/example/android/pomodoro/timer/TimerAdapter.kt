package com.example.android.pomodoro.timer

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.android.pomodoro.R
import com.example.android.pomodoro.databinding.TimerItemBinding

class TimerAdapter(
    private val listener: TimerListener
)
    : ListAdapter<Timer, TimerViewHolder>(TimerDiffCallback())
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TimerItemBinding.inflate(layoutInflater, parent, false)
        return TimerViewHolder(binding,listener)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

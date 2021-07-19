package com.example.android.pomodoro.timer

import android.graphics.drawable.AnimationDrawable
import android.util.Log
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.android.pomodoro.databinding.TimerItemBinding
import com.example.android.pomodoro.longTimeToString
import kotlinx.coroutines.*

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener
) :
    RecyclerView.ViewHolder(binding.root) {
    private var job: Job? = null

    fun bind(timer: Timer) {

        Log.i("MyLog", "bind $timer")

        binding.stopwatchTimer.text = longTimeToString(timer.currentMs)

        if (timer.currentMs != 0L) {
            binding.customTimer.setPeriod(timer.startMs)
            updateCustomTimer(timer.currentMs)
        }

        if (timer.isStarted) {
            startTimer(timer)
        } else {
            job?.cancel()
            stopTimer()
        }

        initButtonsListeners(timer)
    }

    private fun startTimer(timer: Timer) {
        binding.startStopTimerButton.text = STOP
        update(timer)
        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer() {
        binding.startStopTimerButton.text = START

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun updateCustomTimer(time: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            binding.customTimer.setCurrent(time)
            delay(INTERVAL)
        }
    }

    private fun initButtonsListeners(timer: Timer) {
        binding.startStopTimerButton.setOnClickListener {
            if (timer.isStarted) {
                binding.startStopTimerButton.text = START
                stopTimer()
                listener.stop(timer.id, timer.currentMs)
            } else {
                listener.start(timer)
                startTimer(timer)
                binding.startStopTimerButton.text = STOP
            }
        }

        binding.deleteButton.setOnClickListener {
            listener.delete(timer.id)
        }
    }

    private fun update(timer: Timer) {
        job = CoroutineScope(Dispatchers.Main).launch {
            while (timer.isStarted) {
                val currentTime = listener.getCurrentMs(timer.id)
                if (currentTime != null) {
                    if (currentTime <= 0) {
                        binding.stopwatchTimer.text = START_TIME
                        binding.blinkingIndicator.isInvisible = true
                        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
                        binding.startStopTimerButton.isVisible = true
                        job?.cancel()
                    }
                    binding.stopwatchTimer.text = longTimeToString(currentTime)
                    updateCustomTimer(currentTime)
                }
                delay(INTERVAL)
            }
            stopTimer()
        }
    }

    private companion object {
        private const val START_TIME = "00:00:00"
        private const val INTERVAL = 1000L
        private const val START = "START"
        private const val STOP = "STOP"
    }

}
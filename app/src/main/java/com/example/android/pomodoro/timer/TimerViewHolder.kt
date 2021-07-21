package com.example.android.pomodoro.timer

import android.graphics.drawable.AnimationDrawable
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.android.pomodoro.*
import com.example.android.pomodoro.databinding.TimerItemBinding
import kotlinx.coroutines.*

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener
) :
    RecyclerView.ViewHolder(binding.root) {
    private var job: Job? = null

    fun bind(timer: Timer) {
        binding.stopwatchTimer.text = longTimeToString(timer.currentMs)

        if (timer.currentMs > 0L) {
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
            binding.customTimer.setCurrent(time - INTERVAL)
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
//                        binding.constraintLayout.background = itemView.context.getDrawable(R.color.primaryColor_red_400)
                        job?.cancel()
                    }
                    updateCustomTimer(currentTime)
                    binding.stopwatchTimer.text = longTimeToString(currentTime)
                }
                delay(INTERVAL)
            }
            stopTimer()
        }
    }
}
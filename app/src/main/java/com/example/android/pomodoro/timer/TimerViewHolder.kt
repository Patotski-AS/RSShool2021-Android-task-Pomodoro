package com.example.android.pomodoro.timer

import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import android.util.Log
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.android.pomodoro.databinding.TimerItemBinding
import com.example.android.pomodoro.longTimeToString

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener
) :
    RecyclerView.ViewHolder(binding.root) {

    private var countDownTimer: CountDownTimer? = null
    private var workId = 0

    fun bind(timer: Timer) {
        binding.stopwatchTimer.text = longTimeToString(timer.currentMs)
        if (timer.isStarted) {
            startTimer(timer)
        }else stopTimer()
        initButtonsListeners(timer)
    }


    private fun startTimer(timer: Timer) {
        countDownTimer?.cancel()
        countDownTimer = getCountDownTimer(timer)
        countDownTimer?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer() {
        countDownTimer?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()

    }


    private fun getCountDownTimer(timer: Timer): CountDownTimer {

        return object : CountDownTimer(timer.currentMs, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                if (timer.isStarted) {
                    timer.currentMs -= INTERVAL
                    binding.stopwatchTimer.text = longTimeToString(timer.currentMs)
                } else {
                    binding.startStopTimerButton.text = START
                    stopTimer()
                }
            }

            override fun onFinish() {
                binding.stopwatchTimer.text = START_TIME
            }
        }
    }

    private fun initButtonsListeners(timer: Timer) {
        binding.startStopTimerButton.setOnClickListener {
            if (timer.isStarted) {
                binding.startStopTimerButton.text = START
                listener.stop(timer.id, timer.currentMs)
            } else {
                listener.start(timer.id)
                startTimer(timer)
                binding.startStopTimerButton.text = STOP
            }
        }
    }

    private companion object {
        private const val START_TIME = "00:00:00"
        private const val INTERVAL = 10L
        private const val START = "START"
        private const val STOP = "STOP"
    }

}
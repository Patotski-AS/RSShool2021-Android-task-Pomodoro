package com.example.android.pomodoro.timer

import android.os.CountDownTimer
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
        }
        initButtonsListeners(timer)
    }

    private fun startTimer(timer: Timer) {
        countDownTimer?.cancel()
        countDownTimer = getCountDownTimer(timer)
        countDownTimer?.start()
    }

    private fun getCountDownTimer(timer: Timer): CountDownTimer {

        return object : CountDownTimer(timer.currentMs, 10) {

            override fun onTick(millisUntilFinished: Long) {
                if (timer.isStarted) {
                    timer.currentMs -= 10
                    binding.stopwatchTimer.text = longTimeToString(timer.currentMs)
                } else {
                    binding.startStopTimerButton.text = START
                    this.cancel()
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
        private const val UNIT_TEN_MS = 10L
        private const val START = "START"
        private const val STOP = "STOP"
    }

}
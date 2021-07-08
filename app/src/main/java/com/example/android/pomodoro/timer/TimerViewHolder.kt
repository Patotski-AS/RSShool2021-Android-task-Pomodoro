package com.example.android.pomodoro.timer

import android.os.CountDownTimer
import androidx.recyclerview.widget.RecyclerView
import com.example.android.pomodoro.databinding.TimerItemBinding

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener
) :
    RecyclerView.ViewHolder(binding.root) {

    private var countDownTimer: CountDownTimer? = null

    fun bind(timer: Timer) {
        binding.stopwatchTimer.text = textTime(timer.currentMs)
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

    private fun textTime(time: Long): String {
        return if (time <= 0L) START_TIME
        else "%02d:%02d:%02d".format(
            time / 1000 / 3600,
            time / 1000 % 3600 / 60,
            time / 1000 % 60
        )
    }

    private fun getCountDownTimer(timer: Timer): CountDownTimer {

        return object : CountDownTimer(PERIOD, UNIT_TEN_MS) {
            val interval = UNIT_TEN_MS

            override fun onTick(millisUntilFinished: Long) {
                timer.currentMs += interval
                binding.stopwatchTimer.text = textTime(timer.currentMs)
            }

            override fun onFinish() {
                binding.stopwatchTimer.text = textTime(timer.currentMs)
            }
        }
    }

    private fun initButtonsListeners(timer: Timer) {

        binding.startTimerButton.setOnClickListener { listener.start(timer.id) }

    }

    private companion object {
        private const val START_TIME = "00:00:00"
        private const val UNIT_TEN_MS = 10L
        private const val PERIOD = 1000L * 60L * 60L * 24L // Day
    }

}
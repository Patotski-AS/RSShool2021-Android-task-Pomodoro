package com.example.android.pomodoro.timer

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.android.pomodoro.INTERVAL
import com.example.android.pomodoro.START_TIME
import com.example.android.pomodoro.databinding.TimerItemBinding
import com.example.android.pomodoro.longTimeToString
import com.example.android.pomodoro.stringTimeToLong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener
) :
    RecyclerView.ViewHolder(binding.root) {

    private var countDownTimer: CountDownTimer? = null

    fun bind(timer: Timer) {

        binding.stopwatchTimer.text = longTimeToString(timer.remainingMS)

        isVisible(true)

        if (timer.remainingMS <= 0L) finish(timer)
        else {
            binding.customTimer.setPeriod(timer.startMs)
            updateCustomTimer(timer.remainingMS)

            if (timer.isStarted) {
                startTimer(timer)
            } else stopTimer()
        }
        initButtonsListeners(timer)
    }

    private fun startTimer(timer: Timer) {
        binding.startStopTimerButton.text = STOP

        countDownTimer?.cancel()
        countDownTimer = getCountDownTimer(timer)
        countDownTimer?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer() {
        binding.startStopTimerButton.text = START

        countDownTimer?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun getCountDownTimer(timer: Timer): CountDownTimer {

        return object : CountDownTimer(timer.remainingMS, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                if (timer.isStarted) {
                    if (timer.remainingMS <= 0L) {
                        onFinish()
                    }
                    timer.remainingMS = timer.finishTime - System.currentTimeMillis()
                    binding.stopwatchTimer.text = longTimeToString(timer.remainingMS)
                    updateCustomTimer(timer.remainingMS)
                } else {
                    stopTimer()
                }
            }

            override fun onFinish() {
                timer.isStarted = false
                finish(timer)
            }
        }
    }

    private fun finish(timer: Timer) {
        listener.stop(timer.id, stringTimeToLong(START_TIME))
        timer.remainingMS = stringTimeToLong(START_TIME)
        countDownTimer?.cancel()
        binding.stopwatchTimer.text = START_TIME

        isVisible(false)
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
                listener.stop(timer.id, timer.remainingMS)
            } else {
                listener.start(timer.id)
                startTimer(timer)
                binding.startStopTimerButton.text = STOP
            }
        }

        binding.deleteButton.setOnClickListener {
            listener.delete(timer.id)
        }
    }

    private fun isVisible(visible: Boolean) {
        if (visible) {
            binding.layout.setBackgroundColor(Color.TRANSPARENT)
        } else {
            binding.layout.setBackgroundColor(Color.RED)
        }
        binding.startStopTimerButton.isInvisible = !visible
        binding.customTimer.isInvisible = !visible

    }

    private companion object {
        private const val START = "START"
        private const val STOP = "STOP"
    }
}
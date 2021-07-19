package com.example.android.pomodoro.timer

import android.graphics.drawable.AnimationDrawable
import android.util.Log
import androidx.core.view.isInvisible
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
        binding.stopwatchTimer.text = listener.getCurrentMs(timer.id)?.let { longTimeToString(it) }
        if (timer.isStarted) {
            startTimer(timer)
        } else stopTimer()

        if (timer.currentMs != 0L) {
            binding.customTimer.setPeriod(timer.startMs)
            updateCustomTimer(timer.currentMs)
        } else finish()

        initButtonsListeners(timer)
    }

    private fun startTimer(timer: Timer) {
        listener.start(timer)
        update(timer)
        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer() {
        binding.startStopTimerButton.text = START

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }


    private fun finish() {
        binding.startStopTimerButton.isInvisible = true
        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
        binding.stopwatchTimer.text = START_TIME
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
            while (true) {
                binding.stopwatchTimer.text = listener.getCurrentMs(timer.id)?.let {
                    longTimeToString(
                        it
                    )
                }
                listener.getCurrentMs(timer.id)?.let { updateCustomTimer(it) }
                delay(INTERVAL)
            }
        }
    }


//    private fun getCountDownTimer(timer: Timer): CountDownTimer {
//
//        return object : CountDownTimer(timer.startMs, INTERVAL) {
//
//            override fun onTick(millisUntilFinished: Long) {
//                if (timer.isStarted) {
//                    timer.currentMs -= INTERVAL
//                    binding.stopwatchTimer.text = longTimeToString(timer.currentMs)
//                    updateCustomTimer(timer.currentMs)
//                } else {
//                    stopTimer()
//                }
//            }
//
//            override fun onFinish() {
//                finish()
//            }
//        }
//    }


    private companion object {
        private const val START_TIME = "00:00:00"
        private const val INTERVAL = 1000L
        private const val START = "START"
        private const val STOP = "STOP"
    }

}
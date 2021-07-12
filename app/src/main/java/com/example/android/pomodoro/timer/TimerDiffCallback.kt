package com.example.android.pomodoro.timer

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil

class TimerDiffCallback(private val oldList:ArrayList<Timer>, private val newList:ArrayList<Timer>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id==newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].currentMs == newList[newItemPosition].currentMs &&
                oldList[oldItemPosition].isStarted == newList[newItemPosition].isStarted
    }

    @Nullable
    @Override
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }


}
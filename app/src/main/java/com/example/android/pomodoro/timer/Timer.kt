package com.example.android.pomodoro.timer

import android.os.Parcel
import android.os.Parcelable

data class Timer(
    val id: Int,
    var startMs: Long,
    var isStarted: Boolean,
    var currentMs: Long = startMs
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeLong(startMs)
        parcel.writeByte(if (isStarted) 1 else 0)
        parcel.writeLong(currentMs)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Timer> {
        override fun createFromParcel(parcel: Parcel): Timer {
            return Timer(parcel)
        }

        override fun newArray(size: Int): Array<Timer?> {
            return arrayOfNulls(size)
        }
    }
}
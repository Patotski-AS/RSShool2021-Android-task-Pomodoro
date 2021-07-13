package com.example.android.pomodoro.castom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.AttrRes
import com.example.android.pomodoro.R

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var periodMs = 0L
    private var currentMs = 0L
    private var color = 0
    private var color2 = 0
    private var style = FILL
    private val paint = Paint()
    private val paint2 = Paint()

    init {
        if (attrs != null) {
            val styledAttrs = context.theme.obtainStyledAttributes(
                attrs, R.styleable.CustomView, defStyleAttr, 0
            )
            color = styledAttrs.getColor(R.styleable.CustomView_custom_color, Color.RED)
            style = styledAttrs.getInt(R.styleable.CustomView_custom_style, FILL)
            color2 = styledAttrs.getColor(R.styleable.CustomView_custom_color2, color)
            styledAttrs.recycle()
        }

        paint.color = color
        paint.style = if (style == FILL) Paint.Style.FILL else Paint.Style.STROKE
        paint.strokeWidth = 5F

        paint2.color = color2
        paint2.style = Paint.Style.STROKE
        paint2.strokeWidth = 3F

    }

    override fun onDraw(canvas: Canvas) {
        if (periodMs == 0L || currentMs == 0L) return

        val sweepAngle = 360-  currentMs.toFloat() / periodMs * 360
        val y = (height/2).toFloat()
        val x = (width/2).toFloat()

        canvas.drawOval(0f, 0f, width.toFloat(), height.toFloat(), paint2)
        canvas.drawArc(0f, 0f, width.toFloat(), height.toFloat(), -90f, sweepAngle, true, paint)
        canvas.drawLine(0f,y,width.toFloat(),y,paint2)
        canvas.drawLine(x,0f,x,height.toFloat(),paint2)

    }

    fun setCurrent(current: Long) {
        currentMs = current
        invalidate()
    }

    fun setPeriod(period: Long) {
        periodMs = period
    }


    private companion object {
        private const val FILL = 0
        private const val STROKE = 1
    }
}
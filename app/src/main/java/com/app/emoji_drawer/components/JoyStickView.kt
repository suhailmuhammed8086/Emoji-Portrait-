/*
 *
 * Created on : 22-11-2024
 * Author     : Suhail.CP
 *
 * com.app.emoji_drawer.components
 *
 * This file contains the implementation of JoyStickView.kt class.
 */
package com.app.emoji_drawer.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.app.emoji_drawer.components.JoyStickView.TouchEvent.*
import com.app.emoji_drawer.log
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class JoyStickView: View {

    companion object {
        private const val JOY_STICK_SIZE_PERCENTAGE = 0.15f
        private const val BUTTON_SIZE_PERCENTAGE = 0.20f
        private const val INNER_PADDING_PERCENTAGE = 0.02f
        private const val USER_TOUCH_RADIUS = 0.02f
        private const val VALUE_UPDATE_INTERVAL = 100L

        private const val BUTTON_CLICK_THRESHOLD = 1f
        private const val THUMB_THRESHOLD = 20f

    }

    private val trackPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.GRAY
    }
    private val buttonPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }

    private val thumbPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    private var trackRadius = 0f
    private var thumbRadius = 0f
    private var touchRadius = 0f

    private val leftButtonRect = RectF()
    private val rightButtonRect = RectF()
    private val topButtonRect = RectF()
    private val bottomButtonRect = RectF()

    private val thumbPoint = PointF(-1f,-1f)

    private val handler = Handler(Looper.getMainLooper())
    private var listener: Listener? = null


    fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun onDraw(canvas: Canvas) {
        val size = min(width, height)
        thumbRadius = size * JOY_STICK_SIZE_PERCENTAGE
        trackRadius = (size / 2f) - thumbRadius
        val (cx, cy) = getCenterPoint()
        val innerPadding = size * INNER_PADDING_PERCENTAGE
        // Draw Track
        canvas.drawCircle(cx, cy, trackRadius, trackPaint)



        // Buttons
        val buttonSize = size * BUTTON_SIZE_PERCENTAGE

        // Left Button
        leftButtonRect.set(
            cx - trackRadius + innerPadding,
            cy - buttonSize / 2,
            cx + buttonSize - trackRadius + innerPadding,
            cy + buttonSize / 2,
        )
        canvas.drawRect(leftButtonRect, buttonPaint)

        // Right Button
        rightButtonRect.set(
            cx + trackRadius - buttonSize - innerPadding,
            cy - buttonSize / 2,
            cx + trackRadius - innerPadding,
            cy + buttonSize / 2,
        )
        canvas.drawRect(rightButtonRect, buttonPaint)

        // Top Button
        topButtonRect.set(
            cx - buttonSize / 2 + innerPadding,
            cy - trackRadius + innerPadding,
            cx + buttonSize / 2,
            cy - trackRadius + innerPadding + buttonSize,
        )
        canvas.drawRect(topButtonRect, buttonPaint)

        // Bottom Button
        bottomButtonRect.set(
            cx - buttonSize / 2 + innerPadding,
            cy + trackRadius - buttonSize - innerPadding,
            cx + buttonSize / 2,
            cy + trackRadius - innerPadding,
        )
        canvas.drawRect(bottomButtonRect, buttonPaint)


        // JoyStick Section
        if (thumbPoint.x == -1f) {
            thumbPoint.x = cx
        }
        if (thumbPoint.y == -1f) {
            thumbPoint.y = cy
        }
        canvas.drawCircle(thumbPoint.x, thumbPoint.y, thumbRadius, thumbPaint)
    }


    private fun getCenterPoint() = (width/2f) to (height/2f)


    private var touchEvent = IDLE
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            touchEvent = IDLE
            return false
        }
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onDown(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                onMove(x, y)
            }
            MotionEvent.ACTION_UP -> {
                onUp(x, y)
            }
        }
        invalidate()
        return true
    }

    private fun onDown(x: Float, y: Float) {
        findPressedButton(x, y)
        startValueUpdater()
    }
    private fun onMove(x: Float, y: Float) {
        when (touchEvent) {
            THUMB_PRESSED -> {
                val (cx, cy) = getCenterPoint()
                // Checking if the thumb is still in track
                if (isCirclesCollided(cx, cy, trackRadius, x, y, 0f)) {
                    thumbPoint.set(x, y)
                } else {
                    // Thumbs is not in track need to adjust
                    val dx = x - cx
                    val dy = y - cy
                    val radian = atan2(dy,dx)
                    val nX = cx + (trackRadius) * cos(radian)
                    val nY = cy + (trackRadius) * sin(radian)
                    thumbPoint.set(nX, nY)
                }
            }
            LEFT_PRESSED -> {

            }
            RIGHT_PRESSED ->  {

            }
            BOTTOM_PRESSED -> {

            }
            TOP_PRESSED ->  {

            }
            else -> {

            }
        }
    }

    private fun onUp(x: Float, y: Float) {
        when (touchEvent) {
            IDLE ->  {}
            THUMB_PRESSED ->  {
                val (cx,cy) = getCenterPoint()
                thumbPoint.set(cx, cy)
            }
            LEFT_PRESSED ->  {}
            RIGHT_PRESSED ->  {}
            BOTTOM_PRESSED ->  {}
            TOP_PRESSED ->  {}
        }
        stopValueUpdater()
        touchEvent = IDLE
    }

    private fun findPressedButton(x: Float, y: Float) {
        // Checking thumb is pressed or not
        if (isCirclesCollided(thumbPoint.x, thumbPoint.y, thumbRadius, x, y, 0f)) {
            touchEvent = THUMB_PRESSED
        } else {
            if (leftButtonRect.contains(x, y)) {
                touchEvent = LEFT_PRESSED
            } else if (topButtonRect.contains(x, y)) {
                touchEvent = TOP_PRESSED
            } else if (rightButtonRect.contains(x, y)) {
                touchEvent = RIGHT_PRESSED
            } else if (bottomButtonRect.contains(x, y)) {
                touchEvent = BOTTOM_PRESSED
            }
        }
    }

    private fun startValueUpdater() {
        handler.post(valueUpdateRunnable)
    }
    private fun stopValueUpdater() {
        handler.removeCallbacks(valueUpdateRunnable)
    }

    private val valueUpdateRunnable = object : Runnable {
        override fun run() {
            var dx = 0f
            var dy = 0f
            when (touchEvent.log("touchEvent")) {
                THUMB_PRESSED -> {
                    val (cx, cy) = getCenterPoint()
                    val distanceX = thumbPoint.x - cx
                    val distanceY = thumbPoint.y - cy
                    dx = distanceX/ trackRadius * THUMB_THRESHOLD
                    dy = distanceY/ trackRadius * THUMB_THRESHOLD
                }
                LEFT_PRESSED -> {
                    dx = -BUTTON_CLICK_THRESHOLD
                }
                RIGHT_PRESSED -> {
                    dx = BUTTON_CLICK_THRESHOLD
                }
                BOTTOM_PRESSED -> {
                    dy = BUTTON_CLICK_THRESHOLD
                }
                TOP_PRESSED -> {
                    dy = -BUTTON_CLICK_THRESHOLD
                }
                else -> {}
            }
            listener?.onChange(dx,dy)
            handler.postDelayed(this, VALUE_UPDATE_INTERVAL)
        }
    }

    private fun isCirclesCollided(
        c1x: Float,
        c1Y: Float,
        c1r: Float,
        c2x: Float,
        c2Y: Float,
        c2r: Float,
    ): Boolean {
        val dx = c1x - c2x
        val dy = c1Y - c2Y
        val distance = sqrt(dx * dx + dy * dy)
        return distance <= c1r + c2r
    }



    private enum class TouchEvent {
        IDLE,
        THUMB_PRESSED,
        LEFT_PRESSED,
        RIGHT_PRESSED,
        BOTTOM_PRESSED,
        TOP_PRESSED
    }

    interface Listener {
        fun onChange(dx: Float, dy: Float)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes)
}
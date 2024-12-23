/*
 * Copyright (C) 2024 FUJIFILM Corporation. All rights reserved.
 *
 * Created on : 29-11-2024
 * Author     : Suhail.CP
 *
 * com.app.emoji_drawer.components
 *
 * This file contains the implementation of ClickHoldButtons.kt class.
 */
package com.app.emoji_drawer.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

class ClickHoldButtons : AppCompatImageView {

    private var valueUpdateTime = 100L

    private var onPressed: (() -> Unit)? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startValueUpdater()
            }
            MotionEvent.ACTION_UP -> {
                stopValueUpdater()
            }
        }
        return true
    }


    private fun startValueUpdater() {
        handler.post(valueUpdateRunnable)
    }
    private fun stopValueUpdater() {
        handler.removeCallbacks(valueUpdateRunnable)
    }

    private val valueUpdateRunnable = object : Runnable {
        override fun run() {
            onPressed?.invoke()
            handler.postDelayed(this, valueUpdateTime)
        }
    }

    fun setOnPressed(onPressed: () -> Unit) {
        this.onPressed = onPressed
    }


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


}
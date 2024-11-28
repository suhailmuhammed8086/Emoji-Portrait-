/*
 * Copyright (C) 2024 FUJIFILM Corporation. All rights reserved.
 *
 * Created on : 24-11-2024
 * Author     : Suhail.CP
 *
 * com.app.emoji_drawer.ui
 *
 * This file contains the implementation of BaseActivity.kt class.
 */
package com.app.emoji_drawer.ui

import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {
    private var lastClicked = 0L


    override fun setContentView(view: View?) {
        super.setContentView(view)
        observeViewModel()
    }

    abstract fun observeViewModel()

    fun avoidMultipleClick(clickThreshold: Long = 300, action: (() -> Unit)? = null) {
        if (SystemClock.elapsedRealtime() - lastClicked >= clickThreshold) {
            lastClicked = SystemClock.elapsedRealtime()
            action?.invoke()
        }
    }
}
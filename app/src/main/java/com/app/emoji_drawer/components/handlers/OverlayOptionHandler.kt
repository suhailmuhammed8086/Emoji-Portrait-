/*
 * Copyright (C) 2025 FUJIFILM Corporation. All rights reserved.
 *
 * Created on : 08-01-2025
 * Author     : Suhail.CP
 *
 * com.app.emoji_drawer.components.handlers
 *
 * This file contains the implementation of OverlayOptionHandler.kt class.
 */
package com.app.emoji_drawer.components.handlers

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import com.app.emoji_drawer.components.handlers.OverlayOptionHandler.ToggleButtonPositions.*
import com.app.emoji_drawer.databinding.LayoutOverlayOptionsBinding

@SuppressLint("ClickableViewAccessibility")
class OverlayOptionHandler(
    private val binding: LayoutOverlayOptionsBinding
) {

    private var parentLocation = PointF(0f, 0f)


    private var toggleTouchListener = View.OnTouchListener { v, event ->
        if (event == null) return@OnTouchListener false
        val x = event.rawX - parentLocation.x - binding.btOptions.width / 2
        val y = event.rawY - parentLocation.y - binding.btOptions.height / 2
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                binding.btOptions.apply {
                    this.translationX = x
                    this.translationY = y
                }
            }

            MotionEvent.ACTION_UP -> {
                moveToggleToDroppedPosition(x, y)
            }
        }
        return@OnTouchListener true
    }

    private fun moveToggleToDroppedPosition(x: Float, y: Float) {
        val parentWidth = binding.root.measuredWidth
        val parentHeight = binding.root.measuredHeight
        val cX = parentWidth / 2
        val cY = parentHeight / 2
        val toggleButtonWidth = binding.btOptions.measuredWidth
        val toggleButtonHeight = binding.btOptions.measuredHeight
        val toggleButtonMargin = toggleButtonWidth * 0.5

        val position = when {
            x < cX && y < cY -> TOP_LEFT
            x > cX && y < cY -> TOP_RIGHT
            x < cX && y > cY -> BOTTOM_LEFT
            x > cX && y > cY -> BOTTOM_RIGHT
            else -> TOP_LEFT
        }

        val (tx, ty) = when (position) {
            TOP_LEFT -> {
                toggleButtonMargin to toggleButtonMargin
            }
            TOP_RIGHT ->  {
                (parentWidth - toggleButtonMargin - toggleButtonWidth) to toggleButtonMargin
            }
            BOTTOM_LEFT ->  {
                 toggleButtonMargin to (parentHeight - toggleButtonMargin - toggleButtonHeight)
            }
            BOTTOM_RIGHT ->  {
                (parentWidth - toggleButtonMargin - toggleButtonWidth) to (parentHeight - toggleButtonMargin - toggleButtonHeight)
            }
        }

        val animatorSet = AnimatorSet()
        val xAnim = ObjectAnimator.ofFloat(
            binding.btOptions,
            "translationX",
            binding.btOptions.translationX,
            tx.toFloat())
        val yAnim = ObjectAnimator.ofFloat(
            binding.btOptions,
            "translationY",
            binding.btOptions.translationY,
            ty.toFloat())
        animatorSet.playTogether(xAnim, yAnim)
        animatorSet.duration = 300
        animatorSet.start()
    }

    enum class ToggleButtonPositions {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }


    init {
        binding.btOptions.setOnTouchListener(toggleTouchListener)
        binding.root.post {
            val location = IntArray(2)
            binding.btOptions.getLocationInWindow(location)
            parentLocation.set(location[0].toFloat(), location[1].toFloat())
        }
    }

}
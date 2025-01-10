/*
 * Copyright (C) 2025 FUJIFILM Corporation. All rights reserved.
 *
 * Created on : 06-01-2025
 * Author     : Suhail.CP
 *
 * com.app.emoji_drawer.components
 *
 * This file contains the implementation of BitmapEditorView.kt class.
 */
package com.app.emoji_drawer.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.roundToInt

class BitmapEditorView: View {

    private var bitmap: Bitmap? = null

    fun loadBitmap(bitmap: Bitmap) {
//        return
        this.bitmap = bitmap
        invalidate()
    }
    private var updatedbitmap : Bitmap? = null

    var scaleValue = 1.0
    var px = 0
    var py = 0

    private val transPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
//        strokeJoin= Paint.Join.ROUND
//        strokeCap = Paint.Cap.ROUND
//        strokeWidth = 10f
        alpha = 100
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.scale(scaleValue.toFloat(),scaleValue.toFloat(), px.toFloat(), py.toFloat())
        updatedbitmap?.let {
            val dest = RectF(0f, (height - it.height).toFloat(), it.width.toFloat(),(height - it.height).toFloat()+ it.height.toFloat())
            canvas.drawBitmap(it, null, dest, null)
        }
//        canvas.setBitmap(bitmap)
//        canvas.drawCircle(px.toFloat(), py.toFloat(),20f, transPaint)
    }

    private fun updateBitmap() {
        bitmap?.let {
            val newBitmap = it.copy(it.config?: Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(newBitmap)
            canvas.drawCircle(px.toFloat(), py.toFloat(),50f, transPaint)
            updatedbitmap = newBitmap
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                px = x.roundToInt()
                py = y.roundToInt()
            }
            MotionEvent.ACTION_MOVE -> {
                px = x.roundToInt()
                py = y.roundToInt()
            }
            MotionEvent.ACTION_UP -> {
                scaleValue = 1.0
            }
        }
        updateBitmap()
        invalidate()
        return true
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
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)
}
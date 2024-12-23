/*
 * Copyright (C) 2024 FUJIFILM Corporation. All rights reserved.
 *
 * Created on : 24-11-2024
 * Author     : Suhail.CP
 *
 * com.app.emoji_drawer.components
 *
 * This file contains the implementation of DrawingCanvasView.kt class.
 */
package com.app.emoji_drawer.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.SizeF
import android.view.MotionEvent
import android.view.View
import com.app.emoji_drawer.getCenterPoints
import com.app.emoji_drawer.invertHorizontally
import com.app.emoji_drawer.isUnset
import com.app.emoji_drawer.log
import com.app.emoji_drawer.model.DrawingObject

class DrawingCanvasView : View {

    companion object {
        private const val DEFAULT_HEIGHT_PERC = 0.25f
        private const val DEFAULT_WIDTH_PERC = 0.25f
    }

    private val drawingObjects = arrayListOf<DrawingObject>()
    private var selectedObjectPosition = -1

    private fun getSelectedDrawObject() = drawingObjects.getOrNull(selectedObjectPosition)


    override fun onDraw(canvas: Canvas) {
        drawingObjects.forEachIndexed { index, it ->

            if (it.position.isUnset()) {
                val (cx, cy) = canvas.getCenterPoints()
                val x = cx - it.size.width / 2
                val y = cy - it.size.height / 2
                it.position.set(x, y)
            }

            if (index == selectedObjectPosition) {
                drawSelectionBox(canvas, it.position, it.size)
            }
            when (it.type) {
                is DrawingObject.DrawingObjectType.Emoji -> drawBitmap(canvas, it, it.type)
                is DrawingObject.DrawingObjectType.Text -> drawText(canvas, it, it.type)
            }
        }
    }

    private fun drawBitmap(
        canvas: Canvas,
        obj: DrawingObject,
        emoji: DrawingObject.DrawingObjectType.Emoji,
    ) {
        val scale = with(emoji.emoji) {
            val x = obj.size.width / this.width.toFloat()
            val y = obj.size.height / this.height.toFloat()
            PointF(x, y)
        }
        canvas.save()
        canvas.translate(obj.position.x, obj.position.y)
        val matrix = Matrix()
        val px = emoji.emoji.width / 2f
        val py = emoji.emoji.height / 2f
        matrix.postRotate(obj.rotation, px, py)
        matrix.postScale(scale.x, scale.y, 0f, 0f)
        canvas.drawBitmap(emoji.emoji,matrix, null)
        canvas.restore()
    }

    private fun drawText(
        canvas: Canvas,
        obj: DrawingObject,
        emoji: DrawingObject.DrawingObjectType.Text,
    ) {

    }

    private val selectionPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    private val bluePaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    private fun drawSelectionBox(canvas: Canvas, position: PointF, size: SizeF) {
        val rect = RectF(
            position.x,
            position.y,
            position.x + size.width,
            position.y + size.height
        )
        canvas.drawRect(
            rect,
            selectionPaint
        )

        // Horizontal Line
        canvas.drawLine(
            0f,
            rect.centerY(),
            canvas.width.toFloat(),
            rect.centerY(),
            bluePaint
        )
        // Vertical Line
        canvas.drawLine(
            rect.centerX(),
            0f,
            rect.centerX(),
            canvas.height.toFloat(),
            bluePaint
        )

    }


    fun addDrawObject(drawingObject: DrawingObject) {
        if (drawingObject.size.isUnset()) {
            val size = (width * DEFAULT_WIDTH_PERC).coerceAtLeast(20f)
            drawingObject.size = SizeF(size, size)
        }
        drawingObjects.add(drawingObject)
        selectedObjectPosition = drawingObjects.size -1
        invalidate()
    }

    fun updateDrawObjectPosition(dx: Float, dy: Float) {
        if (selectedObjectPosition in 0..<drawingObjects.size) {
            drawingObjects[selectedObjectPosition].apply {
                position.set(position.x + dx, position.y + dy)
            }
            invalidate()
        }
    }

    fun setDrawObjectPosition(x: Float, y: Float) {
        if (selectedObjectPosition in 0..<drawingObjects.size) {
            drawingObjects[selectedObjectPosition].apply {
                position.set(x, y)
            }
            invalidate()
        }
    }

    fun updateRotation(rotation: Float) {
        rotation.log("rotation")
        if (selectedObjectPosition in 0..<drawingObjects.size) {
            drawingObjects[selectedObjectPosition].rotation = rotation
            invalidate()
        }
    }

    fun increaseSize() {
        if (selectedObjectPosition in 0..<drawingObjects.size) {
            drawingObjects[selectedObjectPosition].apply {
                this.size = SizeF(this.size.width + 5f, this.size.height + 5f)
            }
            invalidate()
        }
    }
    fun decreaseSize() {
        if (selectedObjectPosition in 0..<drawingObjects.size) {
            drawingObjects[selectedObjectPosition].apply {
                this.size = SizeF(this.size.width - 5f, this.size.height - 5f)
            }
            invalidate()
        }
    }


    private var selectedItemTouchState = ItemTouchStates.NONE
    enum class ItemTouchStates {
        NONE,
        ITEM,
        RESIZE,
        ANGLE
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return true
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onDown(x,y)
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

    var touchDiffs = PointF(0f, 0f)
    private fun onDown(x: Float, y: Float) {
        // Checking if the touch still on the selected item
        val selectedObj = getSelectedDrawObject()
        if (selectedObj != null) {
            val touchStillOnItem = selectedObj.getRectF().contains(x, y)
            if (touchStillOnItem) {
                val dx = selectedObj.position.x - x
                val dy =  selectedObj.position.y - y
                touchDiffs.set(dx, dy)
            } else {
                selectedObjectPosition = -1
            }
        }
    }

    private fun onMove(x: Float, y: Float) {
        setDrawObjectPosition(x + touchDiffs.x, y + touchDiffs.y)
    }
    private fun onUp(x: Float, y: Float) {
        selectedObjectPosition = drawingObjects.indexOfLast { it.getRectF().contains(x, y) }
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
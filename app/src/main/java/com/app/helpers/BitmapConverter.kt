package com.app.helpers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.app.emoji_drawer.getCenterPoints
import com.app.emoji_drawer.log

object BitmapConverter {
    private const val HEIGHT = 1080
    private const val WIDTH = 1080

    private val paint = Paint().apply {
        this.color = Color.WHITE
        isAntiAlias = false
    }

    // w/h = w1 / h1
    fun generateBitmap(
        text: String,
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        paint.textSize = canvas.height.toFloat() * 0.8f
        while (paint.measureText(text) > canvas.width) {
            paint.textSize -= 1f
        }
        val width = paint.measureText(text).log("width")
        val bounds = Rect()
//        val height = bounds.height() / bounds
        paint.getTextBounds(text, 0, text.length, bounds)
        val x = (canvas.width - width)/2f
        val y = canvas.height * 0.75f
        canvas.drawText(text, x, y, paint)
        canvas.drawLine(0f,y,canvas.width.toFloat(),y, Paint().apply {
            this.color = Color.RED
        })
        return bitmap
    }
}
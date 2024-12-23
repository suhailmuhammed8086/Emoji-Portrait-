/*
 * Copyright (C) 2024 FUJIFILM Corporation. All rights reserved.
 *
 * Created on : 25-11-2024
 * Author     : Suhail.CP
 *
 * com.app.emoji_drawer.model
 *
 * This file contains the implementation of DrawingObject.kt class.
 */
package com.app.emoji_drawer.model

import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.RectF
import android.util.SizeF

data class DrawingObject(
    val id: Int = getRandomId(),
    val type: DrawingObjectType,
    val position: PointF = PointF(-1f,-1f),
    var size: SizeF = SizeF(-1f,-1f),
    var rotation: Float = 0f
) {


    fun getRectF() = RectF(position.x, position.y, position.x + size.width, position.y + size.height)


    sealed class DrawingObjectType {
        data class Emoji(val emoji: Bitmap) : DrawingObjectType()
        data class Text(val text: String) : DrawingObjectType()
    }

    companion object {
        private var idGen = 0
        private fun getRandomId() = idGen++
    }
}


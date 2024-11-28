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

import android.graphics.PointF
import android.util.SizeF

data class DrawingObject(
    val id: Int = getRandomId(),
    val type: DrawingObjectType,
    val position: PointF = PointF(-1f,-1f),
    val size: SizeF = SizeF(-1f,-1f)
) {


    sealed class DrawingObjectType {
        data class Emoji(val emoji: String) : DrawingObjectType()
        data class Text(val text: String) : DrawingObjectType()
    }

    companion object {
        private var idGen = 0
        private fun getRandomId() = idGen++
    }
}


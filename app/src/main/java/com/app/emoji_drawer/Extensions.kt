/*
 * Copyright (C) 2024 FUJIFILM Corporation. All rights reserved.
 *
 * Created on : 22-11-2024
 * Author     : Suhail.CP
 *
 * com.app.emoji_drawer
 *
 * This file contains the implementation of Extensions.kt class.
 */
package com.app.emoji_drawer

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.util.SizeF

fun <T> T.log(key: String = ""): T {
    Log.e("QuickLog", "$key: $this")
    return this
}


fun PointF.isUnset(): Boolean {
    return (x == -1f && y == -1f)
}
fun SizeF.isUnset(): Boolean {
    return (width == -1f && height == -1f)
}

fun Canvas.getCenterPoints(): Pair<Float, Float> {
    return (width/2f) to (height/2f)
}

fun RectF.invertHorizontally() {
    set(right, top, left, bottom)
}
fun Rect.invertHorizontally() {
    set(right, top, left, bottom)
}
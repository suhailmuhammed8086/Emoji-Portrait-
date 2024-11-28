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

import android.content.Context
import android.util.AttributeSet
import android.view.View

class DrawingCanvasView : View {

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
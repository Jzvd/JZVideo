package org.jzvd.jzvideo

import android.view.Surface

interface JZSurfaceInterface {

    fun onSurfaceCreate()

    fun onSurfaceChange()

    fun onSurfaceDestroy()

    fun onSurfaceUpdate()

}
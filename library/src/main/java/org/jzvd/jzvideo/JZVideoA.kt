package org.jzvd.jzvideo

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import cn.jzvd.R

const val TAG = "JZVD"


enum class State {
    IDLE, NORMAL, PREPARING, PREPARING_CHANGE_URL, PREPARING_PLAYING,
    PREPARED, PLAYING, PAUSE, COMPLETE, ERROR
}

open class JZVideoA : RelativeLayout {

    enum class Screen {
        NORMAL, FULLSCREEN, TINY
    }

    //    lateinit var state: State
//
//
    constructor(ctx: Context) : super(ctx) {
        inflate(context, R.layout.jzvideo_a, this)
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        inflate(context, R.layout.jzvideo_a, this)
    }

    fun release() {

    }

    companion object {
        @JvmStatic
        fun releaseAllVideos() {

        }
    }

}

fun releaseAllVideos() {

}
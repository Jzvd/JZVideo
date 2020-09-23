package org.jzvd.jzvideo

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

const val TAG = "JZVD"


open class JZVideoA : RelativeLayout {

    enum class State {
        IDLE, NORMAL, PREPARING, PREPARING_CHANGE_URL, PREPARING_PLAYING,
        PREPARED, PLAYING, PAUSE, COMPLETE, ERROR
    }

    enum class Screen {
        NORMAL, FULLSCREEN, TINY
    }

    lateinit var state: State


    constructor(ctx: Context) : super(ctx) {
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
    }

    companion object {
        @JvmStatic
        fun releaseAllVideos() {

        }
    }

}
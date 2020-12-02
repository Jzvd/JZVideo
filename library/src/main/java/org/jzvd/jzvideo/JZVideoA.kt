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

    var state: State? = null


    constructor(ctx: Context) : super(ctx) {
        inflate(context, R.layout.jzvideo_a, this)
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        inflate(context, R.layout.jzvideo_a, this)
    }

    fun release() {

    }


    fun startVideo() {
        //TODO 添加surface，开始MediaPlayer播放。


    }

    fun addSurface() {

    }

    fun getLayout(): Int {
        return R.layout.jz_video_a;
    }

    companion object {
        @JvmStatic
        fun releaseAllVideos() {

        }
    }

}

fun releaseAllVideos() {

}
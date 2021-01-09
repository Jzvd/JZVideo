package org.jzvd.jzvideo

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.core.view.get
import cn.jzvd.R

const val TAG = "JZVideo"


enum class State {
    IDLE, NORMAL, PREPARING, PREPARING_CHANGE_URL, PREPARING_PLAYING,
    PREPARED, PLAYING, PAUSE, COMPLETE, ERROR
}

enum class Screen {
    NORMAL, FULLSCREEN, TINY
}

open class JZVideoA : RelativeLayout {


    var state: State = State.IDLE

    constructor(ctx: Context) : super(ctx) {
        inflate(context, getLayout(), this)
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        inflate(context, getLayout(), this)
    }

    fun startVideo() {
        //TODO 添加surface，开始MediaPlayer播放。


    }

    fun addSurface() {

    }

    fun getLayout(): Int {
        return R.layout.jz_video_a
    }

}

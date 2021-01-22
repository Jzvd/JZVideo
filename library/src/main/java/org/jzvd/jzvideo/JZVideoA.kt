package org.jzvd.jzvideo

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import cn.jzvd.R
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

const val TAG = "JZVideo"


enum class State {
    IDLE, NORMAL, PREPARING, PREPARING_CHANGE_URL, PREPARING_PLAYING,
    PREPARED, PLAYING, PAUSE, COMPLETE, ERROR
}

enum class Screen {
    NORMAL, FULLSCREEN, TINY
}

class JZVideoA : RelativeLayout, View.OnClickListener {

    var state: State = State.IDLE

    lateinit var url: String
    lateinit var mediaInterfaceClass: KClass<*>
    lateinit var mediaInterface: JZMediaInterface

    lateinit var surfaeView: JZSurfaceView
    var startBtn: ImageView? = null

    constructor(ctx: Context) : super(ctx) {
        inflate(context, getLayout(), this)
        init()
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        inflate(context, getLayout(), this)
        init()
    }

    fun init() {
        surfaeView = findViewById(R.id.surface)
        startBtn = findViewById(R.id.start)
        startBtn!!.setOnClickListener(this)

        startBtn!!.setOnClickListener {
            //TODO 如果正在播放就暂停，如果暂停就播放
            prepare()
        }

    }

    override fun onClick(v: View?) {

    }

    fun setUp(
        url: String,
        mediaInterfaceClass: KClass<*>,
    ) {
        this.url = url
        this.mediaInterfaceClass = mediaInterfaceClass

        val mediaRef = Class.forName(mediaInterfaceClass.java.name).kotlin
        mediaInterface =
            mediaRef.createInstance() as JZMediaInterface//初始化和interface里面的MediaPlayer没有任何关系。
        surfaeView.surfacePrepare(mediaInterface)

    }

    /**
     * 就是startVideo()，和mediaPlayer的函数名一样,prepare表示开始
     */
    fun prepare() {
        mediaInterface.prepare()//MediaPlayer开始工作


    }


    fun onProgress(progress: Int, position: Long, duration: Long) {

    }

    fun getLayout(): Int {
        return R.layout.jz_video_a
    }


    inline fun timerTask(
        crossinline action: TimerTask.() -> Unit
    ) {

    }

}




package org.jzvd.jzvideo

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.RelativeLayout
import cn.jzvd.R
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

class JZVideoA : RelativeLayout {

    var state: State = State.IDLE


    lateinit var mediaInterfaceClass: KClass<*>

    lateinit var surfaeView: JZSurfaceView
    lateinit var mediaInterface: JZMediaInterface

    lateinit var url: String

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


    }

    fun setUp(
        url: String,
        mediaInterfaceClass: KClass<*>,
    ) {
        this.url = url
        this.mediaInterfaceClass = mediaInterfaceClass

        val mediaRef = Class.forName(mediaInterfaceClass.java.name).kotlin
        mediaInterface = mediaRef.createInstance() as JZMediaInterface//初始化和interface里面的MediaPlayer没有任何关系。
        surfaeView.surfacePrepare(mediaInterface)

    }

    /**
     * 就是startVideo()，和mediaPlayer的函数名一样,prepare表示开始
     */
    fun prepare() {
        mediaInterface.prepare()//MediaPlayer开始工作


    }


    //如果没有surface就自己添加surfaceview。
    fun addSurface() {

    }

    fun getLayout(): Int {
        return R.layout.jz_video_a
    }


}




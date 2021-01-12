package org.jzvd.jzvideo

import android.content.Context
import android.util.AttributeSet
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

open class JZVideoA : RelativeLayout {

    var state: State = State.IDLE


    lateinit var mediaInterfaceClass: KClass<*>
    lateinit var surfaceInterfaceClass: KClass<*>
    var surfaceInterface: JZSurfaceInterface? = null
    var mediaInterface: JZMediaInterface? = null
    lateinit var url: String

    constructor(ctx: Context) : super(ctx) {
        inflate(context, getLayout(), this)
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        inflate(context, getLayout(), this)
    }

    fun setUp(
        url: String,
        mediaInterfaceClass: KClass<*>,
        surfaceInterfaceClass: KClass<*>
    ) {
        this.url = url
        this.surfaceInterfaceClass = surfaceInterfaceClass
        this.mediaInterfaceClass = mediaInterfaceClass
    }

    fun init() {
        surfaceInterface = findViewById(R.id.surface)
    }

    fun startVideo() {


        //TODO 添加surface，开始MediaPlayer播放。

        val mediaRef = Class.forName(mediaInterfaceClass.java.name).kotlin
        var ss = mediaRef.createInstance() as JZMediaInterface


//        val constructor: Constructor<JZMediaInterface> =
//            mediaInterfaceClass!!.getConstructor(JZVideoA::class.java)
//        surfaceInterfaceClass!!
//        mediaInterface = constructor.newInstance(this)
//        val constructor: Constructor<cn.jzvd.JZMediaInterface> =
//            mediaInterfaceClass!!.getConstructor(
//                Jzvd::class.java
//            )
//        mediaInterface = constructor.newInstance(this)
    }

    fun addSurface() {

    }

    fun getLayout(): Int {
        return R.layout.jz_video_a
    }

}




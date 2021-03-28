package org.jzvd.jzvideo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import cn.jzvd.Jzvd
import cn.jzvd.R
import java.lang.reflect.Constructor
import java.util.*
import kotlin.reflect.KClass

const val TAG = "JZVideo"


enum class State {
    IDLE, NORMAL, PREPARING, PREPARING_CHANGE_URL, PREPARING_PLAYING,
    PREPARED, PLAYING, PAUSE, COMPLETE, ERROR
}

public class JZVideoA : RelativeLayout, View.OnClickListener {

    var state: State = State.IDLE

    lateinit var url: String
    lateinit var mediaInterfaceClass: KClass<*>
    var mediaInterface: JZMediaInterface? = null

    lateinit var surfaeView: JZSurfaceView//如果没有开始播放时自己添加，如果有直接使用。
    var startBtn: ImageView? = null
    var progressTimer: Timer? = null

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
        startBtn?.setOnClickListener(this)

        startBtn?.setOnClickListener {
            start()
        }

    }

    override fun onClick(v: View?) {
        val i = v?.id
        if (i == R.id.start) {
            when (state) {
                State.NORMAL -> {
                    start()
                }
                State.PAUSE -> {
                    mediaInterface?.start()
                    onStatePlaying()
                }
                State.PLAYING -> {
                    mediaInterface?.pause()
                    onStatePause()
                }
            }
        }
    }

    fun setUp(
        url: String,
        mediaInterfaceClass: KClass<*>,
    ) {
        this.url = url
        this.mediaInterfaceClass = mediaInterfaceClass


        val constructor: Constructor<JZMediaInterface> = mediaInterfaceClass!!.java.getConstructor(
            JZVideoA::class.java
        ) as Constructor<JZMediaInterface>

//        val mediaRef = Class.forName(mediaInterfaceClass.java.name).kotlin
        mediaInterface =
            constructor.newInstance(this) as JZMediaInterface//初始化和interface里面的MediaPlayer没有任何关系。
        surfaeView.prepareSurface(mediaInterface)//这个可能在播放的时候做，setup就是设置数据，没有实际操作。

        state = State.NORMAL
    }

    /**
     * setUp之后
     */
    fun start() {
        Log.d(TAG, "start [" + this.hashCode() + "] ")
        mediaInterface?.prepare()//MediaPlayer开始工作

        onStatePreparing()
    }

    fun onStatePreparing() {
        Log.d(TAG, "onStatePreparing [" + this.hashCode() + "] ")
    }

    fun onStatePlaying() {
        Log.d(TAG, "onStatePlaying [" + this.hashCode() + "] ")
        state = State.PLAYING
    }

    fun onPrepared() {
        Log.d(TAG, "onPrepared [" + this.hashCode() + "] ")
        mediaInterface!!.start()
        state = State.PREPARED
    }

    fun onStatePause() {
        Log.d(TAG, "onStatePause [" + this.hashCode() + "] ")
        state = State.PAUSE
    }

    fun onStateComplete() {
        Log.d(TAG, "onStateComplete [" + this.hashCode() + "] ")
        state = State.COMPLETE
    }

    fun onStateError() {
        Log.d(TAG, "onStateError [" + this.hashCode() + "] ")
        state = State.ERROR
    }

    fun onCompletion() {

    }

    fun setBufferProgress(percent: Int) {

    }

    fun onSeekComplete() {

    }

    fun onError(what: Int, extra: Int) {
        Log.d(TAG, "onError [" + this.hashCode() + "] ")

        state = State.ERROR
    }

    fun onInfo(what: Int, extra: Int) {
        Log.d(TAG, "onInfo [${this.hashCode()}] what:$what extra:$extra")


    }

    fun onProgress(progress: Int, position: Int, duration: Int) {

    }

    fun getLayout(): Int {
        return R.layout.jz_video_a
    }

    fun onVideoSizeChanged(width: Int, height: Int) {
        Log.i(Jzvd.TAG, "onVideoSizeChanged " + " [" + this.hashCode() + "] ")

        surfaeView.setVideoSize(width, height)

    }

    fun startGetProgressTimer() {
        progressTimer?.cancel()
        progressTimer = Timer()
        progressTimer?.schedule(object : TimerTask() {
            override fun run() {
                //取得播放进度，总时间，当前时间
                post {
                    val position: Long? = mediaInterface?.currentPosition
                    val duration: Long? = mediaInterface?.duration
                    if (position != null && duration != null) {
                        var pro: Int
                        if (duration == 0L) {
                            pro = 0
                        } else {
                            pro = position.times(100).div(duration).toInt()//算个加减乘除 这么复杂吗
                        }
                        onProgress(pro, position.toInt(), duration.toInt())
                    }
                }
            }
        }, 0, 300)
    }

}




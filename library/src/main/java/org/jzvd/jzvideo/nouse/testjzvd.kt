package org.jzvd.jzvideo.nouse

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaPlayer
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import cn.jzvd.*
import cn.jzvd.JZTextureView
import cn.jzvd.Jzvd
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * Created by Nathen on 16/7/30.
 */
abstract class Jzvd : FrameLayout, View.OnClickListener, OnSeekBarChangeListener,
    OnTouchListener {
    var state = -1
    var screen = -1
    var jzDataSource: JZDataSource? = null
    var widthRatio = 0
    var heightRatio = 0
    var mediaInterfaceClass: Class<*>? = null
    var mediaInterface: JZMediaInterface? = null
    var positionInList = -1 //很想干掉它
    var videoRotation = 0
    var seekToManulPosition = -1
    var seekToInAdvance: Long = 0
    var startButton: ImageView? = null
    var progressBar: SeekBar? = null
    var fullscreenButton: ImageView? = null
    var currentTimeTextView: TextView? = null
    var totalTimeTextView: TextView? = null
    var textureViewContainer: ViewGroup? = null
    var topContainer: ViewGroup? = null
    var bottomContainer: ViewGroup? = null
    var textureView: JZTextureView? = null
    var preloading = false
    protected var gobakFullscreenTime: Long = 0 //这个应该重写一下，刷新列表，新增列表的刷新，不打断播放，应该是个flag
    protected var gotoFullscreenTime: Long = 0
    protected var UPDATE_PROGRESS_TIMER: Timer? = null
    protected var mScreenWidth = 0
    protected var mScreenHeight = 0
    protected var mAudioManager: AudioManager? = null
    protected var mProgressTimerTask: ProgressTimerTask? = null
    protected var mTouchingProgressBar = false
    protected var mDownX = 0f
    protected var mDownY = 0f
    protected var mChangeVolume = false
    protected var mChangePosition = false
    protected var mChangeBrightness = false
    protected var mGestureDownPosition: Long = 0
    protected var mGestureDownVolume = 0
    protected var mGestureDownBrightness = 0f
    protected var mSeekTimePosition: Long = 0
    protected var jzvdContext: Context? = null
    protected var mCurrentPosition: Long = 0

    /**
     * 如果不在列表中可以不加block
     */
    protected var blockLayoutParams: ViewGroup.LayoutParams? = null
    protected var blockIndex = 0
    protected var blockWidth = 0
    protected var blockHeight = 0

    constructor(context: Context?) : super(context!!) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init(context)
    }

    abstract val layoutId: Int

    fun init(context: Context?) {
        inflate(context, layoutId, this)
        jzvdContext = context
        startButton = findViewById(R.id.start)
        fullscreenButton = findViewById(R.id.fullscreen)
        progressBar = findViewById(R.id.bottom_seek_progress)
        currentTimeTextView = findViewById(R.id.current)
        totalTimeTextView = findViewById(R.id.total)
        bottomContainer = findViewById(R.id.layout_bottom)
        textureViewContainer = findViewById(R.id.surface_container)
        topContainer = findViewById(R.id.layout_top)
        if (startButton == null) {
            startButton = ImageView(context)
        }
        if (fullscreenButton == null) {
            fullscreenButton = ImageView(context)
        }
        if (progressBar == null) {
            progressBar = SeekBar(context)
        }
        if (currentTimeTextView == null) {
            currentTimeTextView = TextView(context)
        }
        if (totalTimeTextView == null) {
            totalTimeTextView = TextView(context)
        }
        if (bottomContainer == null) {
            bottomContainer = LinearLayout(context)
        }
        if (textureViewContainer == null) {
            textureViewContainer = FrameLayout(context!!)
        }
        if (topContainer == null) {
            topContainer = RelativeLayout(context)
        }
        startButton!!.setOnClickListener(this)
        fullscreenButton!!.setOnClickListener(this)
        progressBar!!.setOnSeekBarChangeListener(this)
        bottomContainer!!.setOnClickListener(this)
        textureViewContainer!!.setOnClickListener(this)
        textureViewContainer!!.setOnTouchListener(this)
        mScreenWidth = getContext().resources.displayMetrics.widthPixels
        mScreenHeight = getContext().resources.displayMetrics.heightPixels
        state = Jzvd.STATE_IDLE
    }

    fun setUp(url: String?, title: String?) {
        setUp(JZDataSource(url, title), Jzvd.SCREEN_NORMAL)
    }

    fun setUp(url: String?, title: String?, screen: Int) {
        setUp(JZDataSource(url, title), screen)
    }

    fun setUp(jzDataSource: JZDataSource?, screen: Int) {
        setUp(jzDataSource, screen, JZMediaSystem::class.java)
    }

    fun setUp(url: String?, title: String?, screen: Int, mediaInterfaceClass: Class<*>?) {
        setUp(JZDataSource(url, title), screen, mediaInterfaceClass)
    }

    fun setUp(jzDataSource: JZDataSource?, screen: Int, mediaInterfaceClass: Class<*>?) {
        this.jzDataSource = jzDataSource
        this.screen = screen
        onStateNormal()
        this.mediaInterfaceClass = mediaInterfaceClass
    }

    fun setMediaInterface(mediaInterfaceClass: Class<*>?) {
        reset()
        this.mediaInterfaceClass = mediaInterfaceClass
    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.start) {
            clickStart()
        } else if (i == R.id.fullscreen) {
            clickFullscreen()
        }
    }

    protected fun clickFullscreen() {
        Log.i(Jzvd.TAG, "onClick fullscreen [" + this.hashCode() + "] ")
        if (state == Jzvd.STATE_AUTO_COMPLETE) return
        if (screen == Jzvd.SCREEN_FULLSCREEN) {
            //quit fullscreen
            Jzvd.backPress()
        } else {
            Log.d(Jzvd.TAG, "toFullscreenActivity [" + this.hashCode() + "] ")
            gotoFullscreen()
        }
    }

    protected fun clickStart() {
        Log.i(Jzvd.TAG, "onClick start [" + this.hashCode() + "] ")
        if (jzDataSource == null || jzDataSource!!.urlsMap.isEmpty() || jzDataSource!!.currentUrl == null) {
            Toast.makeText(context, resources.getString(R.string.no_url), Toast.LENGTH_SHORT).show()
            return
        }
        if (state == Jzvd.STATE_NORMAL) {
            if (!jzDataSource!!.currentUrl.toString()
                    .startsWith("file") && !jzDataSource!!.currentUrl.toString().startsWith("/") &&
                !JZUtils.isWifiConnected(context) && !Jzvd.WIFI_TIP_DIALOG_SHOWED
            ) { //这个可以放到std中
                showWifiDialog()
                return
            }
            startVideo()
        } else if (state == Jzvd.STATE_PLAYING) {
            Log.d(Jzvd.TAG, "pauseVideo [" + this.hashCode() + "] ")
            mediaInterface!!.pause()
            onStatePause()
        } else if (state == Jzvd.STATE_PAUSE) {
            mediaInterface!!.start()
            onStatePlaying()
        } else if (state == Jzvd.STATE_AUTO_COMPLETE) {
            startVideo()
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val id = v.id
        if (id == R.id.surface_container) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> touchActionDown(x, y)
                MotionEvent.ACTION_MOVE -> touchActionMove(x, y)
                MotionEvent.ACTION_UP -> touchActionUp()
            }
        }
        return false
    }

    protected fun touchActionUp() {
        Log.i(Jzvd.TAG, "onTouch surfaceContainer actionUp [" + this.hashCode() + "] ")
        mTouchingProgressBar = false
        dismissProgressDialog()
        dismissVolumeDialog()
        dismissBrightnessDialog()
        if (mChangePosition) {
            mediaInterface!!.seekTo(mSeekTimePosition)
            val duration = duration
            val progress = (mSeekTimePosition * 100 / if (duration == 0L) 1 else duration).toInt()
            progressBar!!.progress = progress
        }
        if (mChangeVolume) {
            //change volume event
        }
        startProgressTimer()
    }

    protected fun touchActionMove(x: Float, y: Float) {
        Log.i(Jzvd.TAG, "onTouch surfaceContainer actionMove [" + this.hashCode() + "] ")
        val deltaX = x - mDownX
        var deltaY = y - mDownY
        val absDeltaX = Math.abs(deltaX)
        val absDeltaY = Math.abs(deltaY)
        if (screen == Jzvd.SCREEN_FULLSCREEN) {
            //拖动的是NavigationBar和状态栏
            if (mDownX > JZUtils.getScreenWidth(context) || mDownY < JZUtils.getStatusBarHeight(
                    context
                )
            ) {
                return
            }
            if (!mChangePosition && !mChangeVolume && !mChangeBrightness) {
                if (absDeltaX > Jzvd.THRESHOLD || absDeltaY > Jzvd.THRESHOLD) {
                    cancelProgressTimer()
                    if (absDeltaX >= Jzvd.THRESHOLD) {
                        // 全屏模式下的CURRENT_STATE_ERROR状态下,不响应进度拖动事件.
                        // 否则会因为mediaplayer的状态非法导致App Crash
                        if (state != Jzvd.STATE_ERROR) {
                            mChangePosition = true
                            mGestureDownPosition = currentPositionWhenPlaying
                        }
                    } else {
                        //如果y轴滑动距离超过设置的处理范围，那么进行滑动事件处理
                        if (mDownX < mScreenHeight * 0.5f) { //左侧改变亮度
                            mChangeBrightness = true
                            val lp = JZUtils.getWindow(context).attributes
                            if (lp.screenBrightness < 0) {
                                try {
                                    mGestureDownBrightness = Settings.System.getInt(
                                        context.contentResolver, Settings.System.SCREEN_BRIGHTNESS
                                    ).toFloat()
                                    Log.i(
                                        Jzvd.TAG,
                                        "current system brightness: $mGestureDownBrightness"
                                    )
                                } catch (e: SettingNotFoundException) {
                                    e.printStackTrace()
                                }
                            } else {
                                mGestureDownBrightness = lp.screenBrightness * 255
                                Log.i(
                                    Jzvd.TAG,
                                    "current activity brightness: $mGestureDownBrightness"
                                )
                            }
                        } else { //右侧改变声音
                            mChangeVolume = true
                            mGestureDownVolume =
                                mAudioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
                        }
                    }
                }
            }
        }
        if (mChangePosition) {
            val totalTimeDuration = duration
            if (Jzvd.PROGRESS_DRAG_RATE <= 0) {
                Log.d(Jzvd.TAG, "error PROGRESS_DRAG_RATE value")
                Jzvd.PROGRESS_DRAG_RATE = 1f
            }
//            mSeekTimePosition =
//                (mGestureDownPosition + deltaX * totalTimeDuration / (mScreenWidth * Jzvd.PROGRESS_DRAG_RATE)) as Int.toLong()
            if (mSeekTimePosition > totalTimeDuration) mSeekTimePosition = totalTimeDuration
            val seekTime = JZUtils.stringForTime(mSeekTimePosition)
            val totalTime = JZUtils.stringForTime(totalTimeDuration)
            showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration)
        }
        if (mChangeVolume) {
            deltaY = -deltaY
            val max = mAudioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val deltaV = (max * deltaY * 3 / mScreenHeight).toInt()
            mAudioManager!!.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                mGestureDownVolume + deltaV,
                0
            )
            //dialog中显示百分比
            val volumePercent =
                (mGestureDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight).toInt()
            showVolumeDialog(-deltaY, volumePercent)
        }
        if (mChangeBrightness) {
            deltaY = -deltaY
            val deltaV = (255 * deltaY * 3 / mScreenHeight).toInt()
            val params = JZUtils.getWindow(context).attributes
            if ((mGestureDownBrightness + deltaV) / 255 >= 1) { //这和声音有区别，必须自己过滤一下负值
                params.screenBrightness = 1f
            } else if ((mGestureDownBrightness + deltaV) / 255 <= 0) {
                params.screenBrightness = 0.01f
            } else {
                params.screenBrightness = (mGestureDownBrightness + deltaV) / 255
            }
            JZUtils.getWindow(context).attributes = params
            //dialog中显示百分比
            val brightnessPercent =
                (mGestureDownBrightness * 100 / 255 + deltaY * 3 * 100 / mScreenHeight).toInt()
            showBrightnessDialog(brightnessPercent)
            //                        mDownY = y;
        }
    }

    protected fun touchActionDown(x: Float, y: Float) {
        Log.i(Jzvd.TAG, "onTouch surfaceContainer actionDown [" + this.hashCode() + "] ")
        mTouchingProgressBar = true
        mDownX = x
        mDownY = y
        mChangeVolume = false
        mChangePosition = false
        mChangeBrightness = false
    }

    fun onStateNormal() {
        Log.i(Jzvd.TAG, "onStateNormal " + " [" + this.hashCode() + "] ")
        state = Jzvd.STATE_NORMAL
        cancelProgressTimer()
        if (mediaInterface != null) mediaInterface!!.release()
    }

    fun onStatePreparing() {
        Log.i(Jzvd.TAG, "onStatePreparing " + " [" + this.hashCode() + "] ")
        state = Jzvd.STATE_PREPARING
        resetProgressAndTime()
    }

    fun onStatePreparingPlaying() {
        Log.i(Jzvd.TAG, "onStatePreparingPlaying " + " [" + this.hashCode() + "] ")
        state = Jzvd.STATE_PREPARING_PLAYING
    }

    fun onStatePreparingChangeUrl() {
        Log.i(Jzvd.TAG, "onStatePreparingChangeUrl " + " [" + this.hashCode() + "] ")
        state = Jzvd.STATE_PREPARING_CHANGE_URL
        Jzvd.releaseAllVideos()
        startVideo()

//        mediaInterface.prepare();
    }

    fun changeUrl(jzDataSource: JZDataSource?, seekToInAdvance: Long) {
        this.jzDataSource = jzDataSource
        this.seekToInAdvance = seekToInAdvance
        onStatePreparingChangeUrl()
    }

    fun onPrepared() {
        Log.i(Jzvd.TAG, "onPrepared " + " [" + this.hashCode() + "] ")
        state = Jzvd.STATE_PREPARED
        if (!preloading) {
            mediaInterface!!.start() //这里原来是非县城
            preloading = false
        }
        if (jzDataSource!!.currentUrl.toString().toLowerCase().contains("mp3") ||
            jzDataSource!!.currentUrl.toString().toLowerCase().contains("wma") ||
            jzDataSource!!.currentUrl.toString().toLowerCase().contains("aac") ||
            jzDataSource!!.currentUrl.toString().toLowerCase().contains("m4a") ||
            jzDataSource!!.currentUrl.toString().toLowerCase().contains("wav")
        ) {
            onStatePlaying()
        }
    }

    fun startPreloading() {
        preloading = true
        startVideo()
    }

    /**
     * 如果STATE_PREPARED就播放，如果没准备完成就走正常的播放函数startVideo();
     */
    fun startVideoAfterPreloading() {
        if (state == Jzvd.STATE_PREPARED) {
            mediaInterface!!.start()
        } else {
            preloading = false
            startVideo()
        }
    }

    fun onStatePlaying() {
        Log.i(Jzvd.TAG, "onStatePlaying " + " [" + this.hashCode() + "] ")
        if (state == Jzvd.STATE_PREPARED) { //如果是准备完成视频后第一次播放，先判断是否需要跳转进度。
            if (seekToInAdvance != 0L) {
                mediaInterface!!.seekTo(seekToInAdvance)
                seekToInAdvance = 0
            } else {
                val position = JZUtils.getSavedProgress(context, jzDataSource!!.currentUrl)
                if (position != 0L) {
                    mediaInterface!!.seekTo(position) //这里为什么区分开呢，第一次的播放和resume播放是不一样的。 这里怎么区分是一个问题。然后
                }
            }
        }
        state = Jzvd.STATE_PLAYING
        startProgressTimer()
    }

    fun onStatePause() {
        Log.i(Jzvd.TAG, "onStatePause " + " [" + this.hashCode() + "] ")
        state = Jzvd.STATE_PAUSE
        startProgressTimer()
    }

    fun onStateError() {
        Log.i(Jzvd.TAG, "onStateError " + " [" + this.hashCode() + "] ")
        state = Jzvd.STATE_ERROR
        cancelProgressTimer()
    }

    fun onStateAutoComplete() {
        Log.i(Jzvd.TAG, "onStateAutoComplete " + " [" + this.hashCode() + "] ")
        state = Jzvd.STATE_AUTO_COMPLETE
        cancelProgressTimer()
        progressBar!!.progress = 100
        currentTimeTextView!!.text = totalTimeTextView!!.text
    }

    fun onInfo(what: Int, extra: Int) {
        Log.d(Jzvd.TAG, "onInfo what - $what extra - $extra")
        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            Log.d(Jzvd.TAG, "MEDIA_INFO_VIDEO_RENDERING_START")
            if (state == Jzvd.STATE_PREPARED || state == Jzvd.STATE_PREPARING_CHANGE_URL || state == Jzvd.STATE_PREPARING_PLAYING) {
                onStatePlaying() //开始渲染图像，真正进入playing状态
            }
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            Log.d(Jzvd.TAG, "MEDIA_INFO_BUFFERING_START")
            Jzvd.backUpBufferState = state
            setState(Jzvd.STATE_PREPARING_PLAYING)
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            Log.d(Jzvd.TAG, "MEDIA_INFO_BUFFERING_END")
            if (Jzvd.backUpBufferState != -1) {
                setState(Jzvd.backUpBufferState)
                Jzvd.backUpBufferState = -1
            }
        }
    }

    fun onError(what: Int, extra: Int) {
        Log.e(Jzvd.TAG, "onError " + what + " - " + extra + " [" + this.hashCode() + "] ")
        if (what != 38 && extra != -38 && what != -38 && extra != 38 && extra != -19) {
            onStateError()
            mediaInterface!!.release()
        }
    }

    fun onCompletion() {
        Runtime.getRuntime().gc()
        Log.i(Jzvd.TAG, "onAutoCompletion " + " [" + this.hashCode() + "] ")
        cancelProgressTimer()
        dismissBrightnessDialog()
        dismissProgressDialog()
        dismissVolumeDialog()
        onStateAutoComplete()
        mediaInterface!!.release()
        JZUtils.scanForActivity(context).window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        JZUtils.saveProgress(context, jzDataSource!!.currentUrl, 0)
        if (screen == Jzvd.SCREEN_FULLSCREEN) {
            if (Jzvd.CONTAINER_LIST.size == 0) {
                clearFloatScreen() //直接进入全屏
            } else {
                gotoNormalCompletion()
            }
        }
    }

    fun gotoNormalCompletion() {
        gobakFullscreenTime = System.currentTimeMillis() //退出全屏
        val vg = JZUtils.scanForActivity(jzvdContext).window.decorView as ViewGroup
        vg.removeView(this)
        textureViewContainer!!.removeView(textureView)
        Jzvd.CONTAINER_LIST.getLast().removeViewAt(blockIndex) //remove block
        Jzvd.CONTAINER_LIST.getLast().addView(this, blockIndex, blockLayoutParams)
        Jzvd.CONTAINER_LIST.pop()
        setScreenNormal()
        JZUtils.showStatusBar(jzvdContext)
        JZUtils.setRequestedOrientation(jzvdContext, Jzvd.NORMAL_ORIENTATION)
        JZUtils.showSystemUI(jzvdContext)
    }

    /**
     * 多数表现为中断当前播放
     */
    fun reset() {
        Log.i(Jzvd.TAG, "reset " + " [" + this.hashCode() + "] ")
        if (state == Jzvd.STATE_PLAYING || state == Jzvd.STATE_PAUSE) {
            val position = currentPositionWhenPlaying
            JZUtils.saveProgress(context, jzDataSource!!.currentUrl, position)
        }
        cancelProgressTimer()
        dismissBrightnessDialog()
        dismissProgressDialog()
        dismissVolumeDialog()
        onStateNormal()
        textureViewContainer!!.removeAllViews()
        val mAudioManager =
            applicationContext!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mAudioManager.abandonAudioFocus(Jzvd.onAudioFocusChangeListener)
        JZUtils.scanForActivity(context).window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (mediaInterface != null) mediaInterface!!.release()
    }

    /**
     * 里面的的onState...()其实就是setState...()，因为要可以被复写，所以参考Activity的onCreate(),onState..()的方式看着舒服一些，老铁们有何高见。
     *
     * @param state stateId
     */
    @JvmName("setState1")
    fun setState(state: Int) {
        when (state) {
            Jzvd.STATE_NORMAL -> onStateNormal()
            Jzvd.STATE_PREPARING -> onStatePreparing()
            Jzvd.STATE_PREPARING_PLAYING -> onStatePreparingPlaying()
            Jzvd.STATE_PREPARING_CHANGE_URL -> onStatePreparingChangeUrl()
            Jzvd.STATE_PLAYING -> onStatePlaying()
            Jzvd.STATE_PAUSE -> onStatePause()
            Jzvd.STATE_ERROR -> onStateError()
            Jzvd.STATE_AUTO_COMPLETE -> onStateAutoComplete()
        }
    }

    @JvmName("setScreen1")
    fun setScreen(screen: Int) { //特殊的个别的进入全屏的按钮在这里设置  只有setup的时候能用上
        when (screen) {
            Jzvd.SCREEN_NORMAL -> setScreenNormal()
            Jzvd.SCREEN_FULLSCREEN -> setScreenFullscreen()
            Jzvd.SCREEN_TINY -> setScreenTiny()
        }
    }

    fun startVideo() {
        Log.d(Jzvd.TAG, "startVideo [" + this.hashCode() + "] ")
//        Jzvd.setCurrentJzvd(this)
        try {
            val constructor: Constructor<JZMediaInterface> = mediaInterfaceClass!!.getConstructor(
                Jzvd::class.java
            ) as Constructor<JZMediaInterface>
            mediaInterface = constructor.newInstance(this)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        addTextureView()
        mAudioManager = applicationContext!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mAudioManager!!.requestAudioFocus(
            Jzvd.onAudioFocusChangeListener,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
        )
        JZUtils.scanForActivity(context).window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onStatePreparing()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (screen == Jzvd.SCREEN_FULLSCREEN || screen == Jzvd.SCREEN_TINY) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        if (widthRatio != 0 && heightRatio != 0) {
            val specWidth = MeasureSpec.getSize(widthMeasureSpec)
            val specHeight = (specWidth * heightRatio.toFloat() / widthRatio).toInt()
            setMeasuredDimension(specWidth, specHeight)
            val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(specWidth, MeasureSpec.EXACTLY)
            val childHeightMeasureSpec =
                MeasureSpec.makeMeasureSpec(specHeight, MeasureSpec.EXACTLY)
            getChildAt(0).measure(childWidthMeasureSpec, childHeightMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun addTextureView() {
        Log.d(Jzvd.TAG, "addTextureView [" + this.hashCode() + "] ")
        if (textureView != null) textureViewContainer!!.removeView(textureView)
        textureView = JZTextureView(context.applicationContext)
        textureView!!.surfaceTextureListener = mediaInterface
        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
        textureViewContainer!!.addView(textureView, layoutParams)
    }

    fun clearFloatScreen() {
        JZUtils.showStatusBar(context)
        JZUtils.setRequestedOrientation(context, Jzvd.NORMAL_ORIENTATION)
        JZUtils.showSystemUI(context)
        val vg = JZUtils.scanForActivity(context).window.decorView as ViewGroup
        vg.removeView(this)
        if (mediaInterface != null) mediaInterface!!.release()
        Jzvd.CURRENT_JZVD = null
    }

    fun onVideoSizeChanged(width: Int, height: Int) {
        Log.i(Jzvd.TAG, "onVideoSizeChanged " + " [" + this.hashCode() + "] ")
        if (textureView != null) {
            if (videoRotation != 0) {
                textureView!!.rotation = videoRotation.toFloat()
            }
            textureView!!.setVideoSize(width, height)
        }
    }

    fun startProgressTimer() {
        Log.i(Jzvd.TAG, "startProgressTimer: " + " [" + this.hashCode() + "] ")
        cancelProgressTimer()
        UPDATE_PROGRESS_TIMER = Timer()
        mProgressTimerTask = ProgressTimerTask()
        UPDATE_PROGRESS_TIMER!!.schedule(mProgressTimerTask, 0, 300)
    }

    fun cancelProgressTimer() {
        if (UPDATE_PROGRESS_TIMER != null) {
            UPDATE_PROGRESS_TIMER!!.cancel()
        }
        if (mProgressTimerTask != null) {
            mProgressTimerTask!!.cancel()
        }
    }

    fun onProgress(progress: Int, position: Long, duration: Long) {
//        Log.d(TAG, "onProgress: progress=" + progress + " position=" + position + " duration=" + duration);
        mCurrentPosition = position
        if (!mTouchingProgressBar) {
            if (seekToManulPosition != -1) {
                seekToManulPosition = if (seekToManulPosition > progress) {
                    return
                } else {
                    -1 //这个关键帧有没有必要做
                }
            } else {
                if (progress != 0) progressBar!!.progress = progress
            }
        }
        if (position != 0L) currentTimeTextView!!.text = JZUtils.stringForTime(position)
        totalTimeTextView!!.text = JZUtils.stringForTime(duration)
    }

    fun setBufferProgress(bufferProgress: Int) {
        if (bufferProgress != 0) progressBar!!.secondaryProgress = bufferProgress
    }

    fun resetProgressAndTime() {
        mCurrentPosition = 0
        progressBar!!.progress = 0
        progressBar!!.secondaryProgress = 0
        currentTimeTextView!!.text = JZUtils.stringForTime(0)
        totalTimeTextView!!.text = JZUtils.stringForTime(0)
    }

    val currentPositionWhenPlaying: Long
        get() {
            var position: Long = 0
            if (state == Jzvd.STATE_PLAYING || state == Jzvd.STATE_PAUSE || state == Jzvd.STATE_PREPARING_PLAYING) {
                position = try {
                    mediaInterface!!.currentPosition
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                    return position
                }
            }
            return position
        }
    val duration: Long
        get() {
            var duration: Long = 0
            duration = try {
                mediaInterface!!.duration
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                return duration
            }
            return duration
        }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        Log.i(Jzvd.TAG, "bottomProgress onStartTrackingTouch [" + this.hashCode() + "] ")
        cancelProgressTimer()
        var vpdown = parent
        while (vpdown != null) {
            vpdown.requestDisallowInterceptTouchEvent(true)
            vpdown = vpdown.parent
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        Log.i(Jzvd.TAG, "bottomProgress onStopTrackingTouch [" + this.hashCode() + "] ")
        startProgressTimer()
        var vpup = parent
        while (vpup != null) {
            vpup.requestDisallowInterceptTouchEvent(false)
            vpup = vpup.parent
        }
        if (state != Jzvd.STATE_PLAYING &&
            state != Jzvd.STATE_PAUSE
        ) return
        val time = seekBar.progress * duration / 100
        seekToManulPosition = seekBar.progress
        mediaInterface!!.seekTo(time)
        Log.i(Jzvd.TAG, "seekTo " + time + " [" + this.hashCode() + "] ")
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            //设置这个progres对应的时间，给textview
            val duration = duration
            currentTimeTextView!!.text = JZUtils.stringForTime(progress * duration / 100)
        }
    }

    fun cloneAJzvd(vg: ViewGroup) {
        try {
            val constructor = this@Jzvd.javaClass.getConstructor(
                Context::class.java
            ) as Constructor<Jzvd>
            val jzvd = constructor.newInstance(context)
            jzvd.id = id
            jzvd.minimumWidth = blockWidth
            jzvd.minimumHeight = blockHeight
            vg.addView(jzvd, blockIndex, blockLayoutParams)
            jzvd.setUp(jzDataSource!!.cloneMe(), Jzvd.SCREEN_NORMAL, mediaInterfaceClass)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }
    }

    /**
     * 如果全屏或者返回全屏的视图有问题，复写这两个函数gotoScreenNormal(),根据自己布局的情况重新布局。
     */
    fun gotoFullscreen() {
        gotoFullscreenTime = System.currentTimeMillis()
        var vg = parent as ViewGroup
        jzvdContext = vg.context
        blockLayoutParams = layoutParams
        blockIndex = vg.indexOfChild(this)
        blockWidth = width
        blockHeight = height
        vg.removeView(this)
        cloneAJzvd(vg)
        Jzvd.CONTAINER_LIST.add(vg)
        vg = JZUtils.scanForActivity(jzvdContext).window.decorView as ViewGroup
        val fullLayout: ViewGroup.LayoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        vg.addView(this, fullLayout)
        setScreenFullscreen()
        JZUtils.hideStatusBar(jzvdContext)
        JZUtils.setRequestedOrientation(jzvdContext, Jzvd.FULLSCREEN_ORIENTATION)
        JZUtils.hideSystemUI(jzvdContext) //华为手机和有虚拟键的手机全屏时可隐藏虚拟键 issue:1326
    }

    fun gotoNormalScreen() { //goback本质上是goto
        gobakFullscreenTime = System.currentTimeMillis() //退出全屏
        val vg = JZUtils.scanForActivity(jzvdContext).window.decorView as ViewGroup
        vg.removeView(this)
        //        CONTAINER_LIST.getLast().removeAllViews();
        Jzvd.CONTAINER_LIST.getLast().removeViewAt(blockIndex) //remove block
        Jzvd.CONTAINER_LIST.getLast().addView(this, blockIndex, blockLayoutParams)
        Jzvd.CONTAINER_LIST.pop()
        setScreenNormal() //这块可以放到jzvd中
        JZUtils.showStatusBar(jzvdContext)
        JZUtils.setRequestedOrientation(jzvdContext, Jzvd.NORMAL_ORIENTATION)
        JZUtils.showSystemUI(jzvdContext)
    }

    fun setScreenNormal() { //TODO 这块不对呀，还需要改进，设置flag之后要设置ui，不设置ui这么写没意义呀
        screen = Jzvd.SCREEN_NORMAL
    }

    fun setScreenFullscreen() {
        screen = Jzvd.SCREEN_FULLSCREEN
    }

    fun setScreenTiny() {
        screen = Jzvd.SCREEN_TINY
    }

    //    //重力感应的时候调用的函数，、、这里有重力感应的参数，暂时不能删除
    fun autoFullscreen(x: Float) { //TODO写道demo中
        if (Jzvd.CURRENT_JZVD != null && (state == Jzvd.STATE_PLAYING || state == Jzvd.STATE_PAUSE)
            && screen != Jzvd.SCREEN_FULLSCREEN && screen != Jzvd.SCREEN_TINY
        ) {
            if (x > 0) {
                JZUtils.setRequestedOrientation(context, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            } else {
                JZUtils.setRequestedOrientation(
                    context,
                    ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                )
            }
            gotoFullscreen()
        }
    }

    fun autoQuitFullscreen() {
        if (System.currentTimeMillis() - Jzvd.lastAutoFullscreenTime > 2000 //                && CURRENT_JZVD != null
            && state == Jzvd.STATE_PLAYING && screen == Jzvd.SCREEN_FULLSCREEN
        ) {
            Jzvd.lastAutoFullscreenTime = System.currentTimeMillis()
            Jzvd.backPress()
        }
    }

    fun onSeekComplete() {}
    fun showWifiDialog() {}
    fun showProgressDialog(
        deltaX: Float,
        seekTime: String?, seekTimePosition: Long,
        totalTime: String?, totalTimeDuration: Long
    ) {
    }

    fun dismissProgressDialog() {}
    fun showVolumeDialog(deltaY: Float, volumePercent: Int) {}
    fun dismissVolumeDialog() {}
    fun showBrightnessDialog(brightnessPercent: Int) {}
    fun dismissBrightnessDialog() {}

    //这个函数必要吗
    val applicationContext: Context?
        get() { //这个函数必要吗
            val context = context
            if (context != null) {
                val applicationContext = context.applicationContext
                if (applicationContext != null) {
                    return applicationContext
                }
            }
            return context
        }

    class JZAutoFullscreenListener : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) { //可以得到传感器实时测量出来的变化值
            val x = event.values[SensorManager.DATA_X]
            val y = event.values[SensorManager.DATA_Y]
            val z = event.values[SensorManager.DATA_Z]
            //过滤掉用力过猛会有一个反向的大数值
            if (x < -12 || x > 12) {
                if (System.currentTimeMillis() - Jzvd.lastAutoFullscreenTime > 2000) {
                    if (Jzvd.CURRENT_JZVD != null) Jzvd.CURRENT_JZVD.autoFullscreen(
                        x
                    )
                    Jzvd.lastAutoFullscreenTime = System.currentTimeMillis()
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    /**
     * 这个线程有原罪。为什么mediaplayer没有回调。
     */
    inner class ProgressTimerTask : TimerTask() {
        override fun run() {
            if (state == Jzvd.STATE_PLAYING || state == Jzvd.STATE_PAUSE || state == Jzvd.STATE_PREPARING_PLAYING) {
//                Log.v(TAG, "onProgressUpdate " + "[" + this.hashCode() + "] ");
                post {
                    val position = currentPositionWhenPlaying
                    val duration = duration
                    val progress = (position * 100 / if (duration == 0L) 1 else duration).toInt()
                    onProgress(progress, position, duration)
                }
            }
        }
    }

    companion object {
        const val TAG = "JZVD"
        const val SCREEN_NORMAL = 0
        const val SCREEN_FULLSCREEN = 1
        const val SCREEN_TINY = 2
        const val STATE_IDLE = -1
        const val STATE_NORMAL = 0
        const val STATE_PREPARING = 1
        const val STATE_PREPARING_CHANGE_URL = 2
        const val STATE_PREPARING_PLAYING = 3
        const val STATE_PREPARED = 4
        const val STATE_PLAYING = 5
        const val STATE_PAUSE = 6
        const val STATE_AUTO_COMPLETE = 7
        const val STATE_ERROR = 8
        const val VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER = 0 //DEFAULT
        const val VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT = 1
        const val VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP = 2
        const val VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL = 3
        const val THRESHOLD = 80
        var CURRENT_JZVD: Jzvd? = null
        var CONTAINER_LIST = LinkedList<ViewGroup>()
        var TOOL_BAR_EXIST = true
        var FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        var NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        var SAVE_PROGRESS = true
        var WIFI_TIP_DIALOG_SHOWED = false
        var VIDEO_IMAGE_DISPLAY_TYPE = 0
        var lastAutoFullscreenTime: Long = 0
        var ON_PLAY_PAUSE_TMP_STATE = 0 //这个考虑不放到库里，去自定义
        var backUpBufferState = -1
        var PROGRESS_DRAG_RATE = 1f //进度条滑动阻尼系数 越大播放进度条滑动越慢
        var onAudioFocusChangeListener: OnAudioFocusChangeListener =
            object : OnAudioFocusChangeListener {
                //是否新建个class，代码更规矩，并且变量的位置也很尴尬
                override fun onAudioFocusChange(focusChange: Int) {
                    when (focusChange) {
                        AudioManager.AUDIOFOCUS_GAIN -> {
                        }
                        AudioManager.AUDIOFOCUS_LOSS -> {
                            Jzvd.releaseAllVideos()
                            Log.d(Jzvd.TAG, "AUDIOFOCUS_LOSS [" + this.hashCode() + "]")
                        }
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                            try {
                                val player: Jzvd = Jzvd.CURRENT_JZVD
                                if (player != null && player.state == Jzvd.STATE_PLAYING) {
                                    player.startButton.performClick()
                                }
                            } catch (e: IllegalStateException) {
                                e.printStackTrace()
                            }
                            Log.d(
                                Jzvd.TAG,
                                "AUDIOFOCUS_LOSS_TRANSIENT [" + this.hashCode() + "]"
                            )
                        }
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                        }
                    }
                }
            }

        /**
         * 增加准备状态逻辑
         */
        fun goOnPlayOnResume() {
            if (Jzvd.CURRENT_JZVD != null) {
                if (Jzvd.CURRENT_JZVD.state == Jzvd.STATE_PAUSE) {
                    if (Jzvd.ON_PLAY_PAUSE_TMP_STATE == Jzvd.STATE_PAUSE) {
                        Jzvd.CURRENT_JZVD.onStatePause()
                        Jzvd.CURRENT_JZVD.mediaInterface.pause()
                    } else {
                        Jzvd.CURRENT_JZVD.onStatePlaying()
                        Jzvd.CURRENT_JZVD.mediaInterface.start()
                    }
                    Jzvd.ON_PLAY_PAUSE_TMP_STATE = 0
                } else if (Jzvd.CURRENT_JZVD.state == Jzvd.STATE_PREPARING) {
                    //准备状态暂停后的
                    Jzvd.CURRENT_JZVD.startVideo()
                }
                if (Jzvd.CURRENT_JZVD.screen == Jzvd.SCREEN_FULLSCREEN) {
//                    JZUtils.hideStatusBar(Jzvd.CURRENT_JZVD.jzvdContext)
//                    JZUtils.hideSystemUI(Jzvd.CURRENT_JZVD.jzvdContext)
                }
            }
        }

        /**
         * 增加准备状态逻辑
         */
        fun goOnPlayOnPause() {
            if (Jzvd.CURRENT_JZVD != null) {
                if (Jzvd.CURRENT_JZVD.state == Jzvd.STATE_AUTO_COMPLETE || Jzvd.CURRENT_JZVD.state == Jzvd.STATE_NORMAL || Jzvd.CURRENT_JZVD.state == Jzvd.STATE_ERROR) {
                    Jzvd.releaseAllVideos()
                } else if (Jzvd.CURRENT_JZVD.state == Jzvd.STATE_PREPARING) {
                    //准备状态暂停的逻辑
                    Jzvd.setCurrentJzvd(Jzvd.CURRENT_JZVD)
                    Jzvd.CURRENT_JZVD.state = Jzvd.STATE_PREPARING
                } else {
                    Jzvd.ON_PLAY_PAUSE_TMP_STATE = Jzvd.CURRENT_JZVD.state
                    Jzvd.CURRENT_JZVD.onStatePause()
                    Jzvd.CURRENT_JZVD.mediaInterface.pause()
                }
            }
        }

        fun startFullscreenDirectly(
            context: Context?,
            _class: Class<*>?,
            url: String?,
            title: String?
        ) {
            Jzvd.startFullscreenDirectly(context, _class, JZDataSource(url, title))
        }

        fun startFullscreenDirectly(
            context: Context?,
            _class: Class<*>,
            jzDataSource: JZDataSource?
        ) {
            JZUtils.hideStatusBar(context)
            JZUtils.setRequestedOrientation(context, Jzvd.FULLSCREEN_ORIENTATION)
            JZUtils.hideSystemUI(context)
            val vp = JZUtils.scanForActivity(context).window.decorView as ViewGroup
            try {
                val constructor: Constructor<Jzvd> = _class.getConstructor(
                    Context::class.java
                ) as Constructor<Jzvd>
                val jzvd = constructor.newInstance(context)
                val lp = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                vp.addView(jzvd, lp)
                jzvd.setUp(jzDataSource, JzvdStd.SCREEN_FULLSCREEN)
                jzvd.startVideo()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun releaseAllVideos() {
            Log.d(Jzvd.TAG, "releaseAllVideos")
            if (Jzvd.CURRENT_JZVD != null) {
                Jzvd.CURRENT_JZVD.reset()
                Jzvd.CURRENT_JZVD = null
            }
        }

        fun backPress(): Boolean {
            Log.i(Jzvd.TAG, "backPress")
            if (Jzvd.CONTAINER_LIST.size != 0 && Jzvd.CURRENT_JZVD != null) { //判断条件，因为当前所有goBack都是回到普通窗口
                Jzvd.CURRENT_JZVD.gotoNormalScreen()
                return true
            } else if (Jzvd.CONTAINER_LIST.size == 0 && Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_NORMAL) { //退出直接进入的全屏
                Jzvd.CURRENT_JZVD.clearFloatScreen()
                return true
            }
            return false
        }

        fun setCurrentJzvd(jzvd: Jzvd) {
            if (Jzvd.CURRENT_JZVD != null) Jzvd.CURRENT_JZVD.reset()
            Jzvd.CURRENT_JZVD = jzvd
        }

        fun setTextureViewRotation(rotation: Int) {
            if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.textureView != null) {
                Jzvd.CURRENT_JZVD.textureView.setRotation(rotation.toFloat())
            }
        }

        fun setVideoImageDisplayType(type: Int) {
            Jzvd.VIDEO_IMAGE_DISPLAY_TYPE = type
            if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.textureView != null) {
                Jzvd.CURRENT_JZVD.textureView.requestLayout()
            }
        }
    }
}
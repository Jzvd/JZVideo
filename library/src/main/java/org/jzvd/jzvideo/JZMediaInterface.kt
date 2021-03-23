package org.jzvd.jzvideo

import android.os.Handler
import android.os.HandlerThread
import android.view.SurfaceHolder

abstract class JZMediaInterface(var jzVideoA: JZVideoA?) {

    var mMediaHandlerThread: HandlerThread? = null
    var mMediaHandler: Handler? = null
    var handler: Handler? = null
    abstract fun start()
    abstract fun prepare()
    abstract fun pause()
    abstract val isPlaying: Boolean

    abstract fun seekTo(time: Long)
    abstract fun release()
    abstract val currentPosition: Long
    abstract val duration: Long

    abstract fun setVolume(leftVolume: Float, rightVolume: Float)
    abstract fun setSpeed(speed: Float)
    abstract fun setSurface(surface: SurfaceHolder)


}